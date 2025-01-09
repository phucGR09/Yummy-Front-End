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
    waiting_confirmation, // Đang chờ xác nhận
    confirmed, // Đã xác nhận
    waiting_delivery, // Đang chờ lấy hàng
    delivering, // Đang giao
    delivered, // Đã giao
    cancelled // Đã hủy
}

class OrderModel {
    // Danh sách các trạng thái đơn hàng
    private val _waitingConfirmation = MutableStateFlow<List<Order>>(emptyList())
    private val _confirmed = MutableStateFlow<List<Order>>(emptyList())
    private val _waitingDelivery = MutableStateFlow<List<Order>>(emptyList())
    private val _delivering = MutableStateFlow<List<Order>>(emptyList())
    private val _delivered = MutableStateFlow<List<Order>>(emptyList())
    private val _cancelled = MutableStateFlow<List<Order>>(emptyList())

    val waitingConfirmation: StateFlow<List<Order>> = _waitingConfirmation
    val confirmed: StateFlow<List<Order>> = _confirmed
    val waitingDelivery: StateFlow<List<Order>> = _waitingDelivery
    val delivering: StateFlow<List<Order>> = _delivering
    val delivered: StateFlow<List<Order>> = _delivered
    val cancelled: StateFlow<List<Order>> = _cancelled

    // Hàm lấy danh sách đơn hàng từ server
    fun getOrdersList(restaurantId: Int) {
        val serverData = listOf(
            Order(1, restaurantId, 101, 201, LocalDateTime.now().minusHours(3), 50000.0, OrderStatus.confirmed),
            Order(2, restaurantId, 102, 202, LocalDateTime.now().minusHours(1), 30000.0, OrderStatus.delivering),
            Order(3, restaurantId, 103, 203, LocalDateTime.now().minusDays(1), 40000.0, OrderStatus.delivered),
            Order(4, restaurantId, 104, 204, LocalDateTime.now().minusHours(2), 20000.0, OrderStatus.cancelled),
            Order(5, restaurantId, 105, 205, LocalDateTime.now(), 45000.0, OrderStatus.confirmed),
            Order(6, restaurantId, 105, 205, LocalDateTime.now(), 45000.0, OrderStatus.waiting_confirmation),
            Order(7, restaurantId, 105, 205, LocalDateTime.now(), 45000.0, OrderStatus.waiting_confirmation)
        )
        updateOrders(serverData)
    }

    // Hàm cập nhật các danh sách theo trạng thái
    private fun updateOrders(orders: List<Order>) {
        _waitingConfirmation.value = orders.filter { it.orderStatus == OrderStatus.waiting_confirmation }.sortedBy { it.orderTime }
        _confirmed.value = orders.filter { it.orderStatus == OrderStatus.confirmed }.sortedBy { it.orderTime }
        _waitingDelivery.value = orders.filter { it.orderStatus == OrderStatus.waiting_delivery }.sortedBy { it.orderTime }
        _delivering.value = orders.filter { it.orderStatus == OrderStatus.delivering }.sortedBy { it.orderTime }
        _delivered.value = orders.filter { it.orderStatus == OrderStatus.delivered }.sortedBy { it.orderTime }
        _cancelled.value = orders.filter { it.orderStatus == OrderStatus.cancelled }.sortedBy { it.orderTime }
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
        if (setStatus(order, OrderStatus.cancelled)) {
            _waitingConfirmation.update { it.filter { o -> o.orderId != order.orderId } }
            _cancelled.update { it + order.copy(orderStatus = OrderStatus.cancelled) }
        }
    }

    // Hàm xác nhận đơn hàng
    fun confirmOrder(order: Order) {
        if (setStatus(order, OrderStatus.confirmed)) {
            _waitingConfirmation.update { it.filter { o -> o.orderId != order.orderId } }
            _confirmed.update { it + order.copy(orderStatus = OrderStatus.confirmed) }
        }
    }
}
