package com.example.yummy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class OrderSellerViewModel(private val orderModel: OrderModel) : ViewModel() {
    val waitingDelivery: StateFlow<List<Order>> = orderModel.waitingDelivery
    val delivering: StateFlow<List<Order>> = orderModel.delivering
    val waitingConfirmation: StateFlow<List<Order>> = orderModel.waitingConfirmation

    // Hàm gọi danh sách đơn hàng từ Model
    fun fetchOrders(restaurantId: Int) {
        orderModel.getOrdersList(restaurantId)
    }
}



