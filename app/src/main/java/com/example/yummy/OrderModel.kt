package com.example.yummy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

// Data class cho Order
data class Order(
    val orderId: Int,               // ID của đơn hàng
    val restaurantId: Int,          // ID của nhà hàng
    val driverId: Int,              // ID của tài xế (có thể -1 nếu chưa có tài xế)
    val customerId: Int,            // ID của khách hàng
    val driverUsername: String?,    // Tên người dùng của tài xế (nullable nếu chưa có tài xế)
    val customerUsername: String,   // Tên người dùng của khách hàng (bắt buộc)
    val orderTime: LocalDateTime,   // Thời gian đặt hàng
    val totalPrice: Double,         // Tổng giá trị đơn hàng
    var orderStatus: OrderStatus    // Trạng thái đơn hàng
)


// Enum class cho trạng thái đơn hàng
enum class OrderStatus {
    WAITING_RESTAURANT_CONFIRMATION, // Đang chờ xác nhận
    RESTAURANT_CONFIRMED, // Đã xác nhận
    WAITING_DELIVERY, // Đang chờ lấy hàng
    DELIVERING, // Đang giao
    DELIVERED, // Đã giao
    CANCELLED // Đã hủy
}

class OrderModel {
    // Danh sách các trạng thái đơn hàng
    private val _WAITING_RESTAURANT_CONFIRMATION = MutableStateFlow<List<Order>>(emptyList())
    private val _RESTAURANT_CONFIRMED = MutableStateFlow<List<Order>>(emptyList())
    private val _WAITING_DELIVERY = MutableStateFlow<List<Order>>(emptyList())
    private val _DELIVERING = MutableStateFlow<List<Order>>(emptyList())
    private val _DELIVERED = MutableStateFlow<List<Order>>(emptyList())
    private val _CANCELLED = MutableStateFlow<List<Order>>(emptyList())


    private val apiService = ApiClient.instance.create(ApiService::class.java)

    val WAITING_RESTAURANT_CONFIRMATION: StateFlow<List<Order>> = _WAITING_RESTAURANT_CONFIRMATION
    val RESTAURANT_CONFIRMED: StateFlow<List<Order>> = _RESTAURANT_CONFIRMED
    val WAITING_DELIVERY: StateFlow<List<Order>> = _WAITING_DELIVERY
    val DELIVERING: StateFlow<List<Order>> = _DELIVERING
    val DELIVERED: StateFlow<List<Order>> = _DELIVERED
    val cancelled: StateFlow<List<Order>> = _CANCELLED

    private val _orders = MutableStateFlow<List<OrderDetails>>(emptyList())
    val orders: StateFlow<List<OrderDetails>> = _orders

    suspend fun fetchAndCategorizeOrders(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Lấy restaurantId từ SharedData hoặc ApiClient
                val restaurantId = SharedData.restaurantId.takeIf { it > 0 } ?: ApiClient.getRestaurantId()

                // Gọi API lấy danh sách đơn hàng từ server
                val response = apiService.getOrdersByRestaurantId(restaurantId)
                if (response.isSuccessful) {
                    val ordersFromServer = response.body()?.result.orEmpty()

                    // Chuyển đổi và phân loại đơn hàng
                    val convertedOrders = ordersFromServer.map { orderDetails ->
                        Order(
                            orderId = orderDetails.id,
                            restaurantId = orderDetails.restaurant.id,
                            driverId = orderDetails.driver?.id ?: -1,
                            customerId = orderDetails.customer.id,
                            driverUsername = orderDetails.driver?.user?.username, // Lấy thông tin tài xế nếu có
                            customerUsername = orderDetails.customer.user.username, // Tên người dùng của khách hàng
                            orderTime = orderDetails.orderTime,
                            totalPrice = orderDetails.totalPrice,
                            orderStatus = when (orderDetails.status) {
                                "WAITING_RESTAURANT_CONFIRMATION" -> OrderStatus.WAITING_RESTAURANT_CONFIRMATION
                                "RESTAURANT_CONFIRMED" -> OrderStatus.RESTAURANT_CONFIRMED
                                "WAITING_DELIVERY" -> OrderStatus.WAITING_DELIVERY
                                "DELIVERING" -> OrderStatus.DELIVERING
                                "DELIVERED" -> OrderStatus.DELIVERED
                                "CANCELLED" -> OrderStatus.CANCELLED
                                else -> throw IllegalArgumentException("Unknown status: ${orderDetails.status}")
                            }
                        )
                    }


                    // Phân loại đơn hàng theo trạng thái
                    updateOrders(convertedOrders)

                    println("Lấy và phân loại đơn hàng thành công: ${convertedOrders.size} đơn hàng")
                    return@withContext true
                } else {
                    println("Lỗi khi lấy đơn hàng: ${response.errorBody()?.string()}")
                    return@withContext false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    suspend fun updateOrderStatus(order: Order, newStatus: OrderStatus): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = OrderUpdateRequest(
                    id = order.orderId,
                    restaurantId = order.restaurantId,
                    driverUsername = order.driverUsername,
                    customerUsername = order.customerUsername,
                    orderTime = order.orderTime.toString(),
                    totalPrice = order.totalPrice,
                    status = newStatus.name
                )

                val response = apiService.updateOrder(request)
                if (response.isSuccessful) {
                    val updatedOrder = response.body()?.result
                    println("Cập nhật trạng thái đơn hàng thành công: ${updatedOrder?.id} -> ${updatedOrder?.status}")
                    // Có thể thêm logic cập nhật trạng thái nội bộ nếu cần
                    true
                } else {
                    println("Lỗi khi cập nhật trạng thái đơn hàng: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }




    // Hàm lấy danh sách đơn hàng từ server
    fun getOrdersList(restaurantId: Int) {

    }

    // Hàm cập nhật các danh sách theo trạng thái
    private fun updateOrders(orders: List<Order>) {
        _WAITING_RESTAURANT_CONFIRMATION.value = orders.filter { it.orderStatus == OrderStatus.WAITING_RESTAURANT_CONFIRMATION }.sortedBy { it.orderTime }
        _RESTAURANT_CONFIRMED.value = orders.filter { it.orderStatus == OrderStatus.RESTAURANT_CONFIRMED }.sortedBy { it.orderTime }
        _WAITING_DELIVERY.value = orders.filter { it.orderStatus == OrderStatus.WAITING_DELIVERY}.sortedBy { it.orderTime }
        _DELIVERING.value = orders.filter { it.orderStatus == OrderStatus.DELIVERING}.sortedBy { it.orderTime }
        _DELIVERED.value = orders.filter { it.orderStatus == OrderStatus.DELIVERED }.sortedBy { it.orderTime }
        _CANCELLED.value = orders.filter { it.orderStatus == OrderStatus.CANCELLED}.sortedBy { it.orderTime }
    }

    // Hàm giả lập cập nhật trạng thái lên server
    fun setStatus(order: Order, newStatus: OrderStatus): Boolean {
        // Giả lập thành công việc gọi API lên server
        println("Cập nhật trạng thái của đơn hàng #${order.orderId} thành $newStatus trên server...")
        // Giả lập việc cập nhật thành công
        return true
    }

    // Hàm từ chối đơn hàng
    fun rejectOrder(order: Order) {
        if (setStatus(order, OrderStatus.CANCELLED)) {
            _WAITING_RESTAURANT_CONFIRMATION.update { it.filter { o -> o.orderId != order.orderId } }
            _CANCELLED.update { it + order.copy(orderStatus = OrderStatus.CANCELLED) }
        }
    }

    // Hàm xác nhận đơn hàng
    fun confirmOrder(order: Order) {
        if (setStatus(order, OrderStatus.RESTAURANT_CONFIRMED)) {
            _WAITING_RESTAURANT_CONFIRMATION.update { it.filter { o -> o.orderId != order.orderId } }
            _RESTAURANT_CONFIRMED.update { it + order.copy(orderStatus = OrderStatus.RESTAURANT_CONFIRMED) }
        }
    }

    fun getTotalRevenue(): Double {
        return _DELIVERED.value.sumOf { it.totalPrice }
    }

    fun getTotalCompletedOrders(): Int {
        return _DELIVERED.value.size
    }


    fun getAverageRevenuePerOrder(): Double {
        val completedOrders = _DELIVERED.value.size
        return if (completedOrders > 0) {
            getTotalRevenue() / completedOrders
        } else {
            0.0
        }
    }
}
