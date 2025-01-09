package com.example.yummy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
class OrderSellerViewModel(private val orderModel: OrderModel) : ViewModel() {
    val delivering: StateFlow<List<Order>> = orderModel.delivering
    val waitingConfirmation: StateFlow<List<Order>> = orderModel.waitingConfirmation
    val confirmed: StateFlow<List<Order>> = orderModel.confirmed

    // Hàm gọi danh sách đơn hàng từ Model
    fun fetchOrders(restaurantId: Int) {
        orderModel.getOrdersList(restaurantId)
    }

    // Hàm từ chối đơn hàng
    fun rejectOrder(order: Order) {
        orderModel.rejectOrder(order)
    }

    // Hàm xác nhận đơn hàng
    fun confirmOrder(order: Order) {
        orderModel.confirmOrder(order)
    }

    fun rejectOrderWithReason(orderId: Int, reason: String) {
        val order = waitingConfirmation.value.find { it.orderId == orderId }
        if (order != null) {
            println("Lý do hủy đơn hàng #$orderId: $reason") // Log lý do
            orderModel.rejectOrder(order)
        }
    }
}



