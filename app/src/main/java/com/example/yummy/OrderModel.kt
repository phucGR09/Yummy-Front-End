package com.example.yummy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

// Data class cho Order
data class Order(
    val orderId: Int,
    val restaurantId: Int,
    val driverId: Int,
    val customerId: Int,
    val orderTime: LocalDateTime,
    val totalPrice: Double,
    var orderStatus: OrderStatus
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

    val WAITING_RESTAURANT_CONFIRMATION: StateFlow<List<Order>> = _WAITING_RESTAURANT_CONFIRMATION
    val RESTAURANT_CONFIRMED: StateFlow<List<Order>> = _RESTAURANT_CONFIRMED
    val WAITING_DELIVERY: StateFlow<List<Order>> = _WAITING_DELIVERY
    val DELIVERING: StateFlow<List<Order>> = _DELIVERING
    val DELIVERED: StateFlow<List<Order>> = _DELIVERED
    val cancelled: StateFlow<List<Order>> = _CANCELLED

    // Hàm lấy danh sách đơn hàng từ server
    fun getOrdersList(restaurantId: Int) {
        val serverData = listOf(
            Order(1, restaurantId, 101, 201, LocalDateTime.now().minusHours(3), 50000.0, OrderStatus.RESTAURANT_CONFIRMED),
            Order(2, restaurantId, 102, 202, LocalDateTime.now().minusHours(1), 30000.0, OrderStatus.DELIVERING),
            Order(3, restaurantId, 103, 203, LocalDateTime.now().minusDays(1), 40000.0, OrderStatus.DELIVERED),
            Order(4, restaurantId, 104, 204, LocalDateTime.now().minusHours(2), 20000.0, OrderStatus.CANCELLED),
            Order(5, restaurantId, 105, 205, LocalDateTime.now(), 45000.0, OrderStatus.RESTAURANT_CONFIRMED),
            Order(6, restaurantId, 105, 205, LocalDateTime.now(), 45000.0, OrderStatus.WAITING_RESTAURANT_CONFIRMATION),
            Order(7, restaurantId, 105, 205, LocalDateTime.now(), 45000.0, OrderStatus.WAITING_RESTAURANT_CONFIRMATION)
        )
        updateOrders(serverData)
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
