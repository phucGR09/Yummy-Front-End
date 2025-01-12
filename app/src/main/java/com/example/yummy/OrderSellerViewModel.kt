package com.example.yummy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderSellerViewModel(private val orderModel: OrderModel) : ViewModel() {
    val delivering: StateFlow<List<Order>> = orderModel.DELIVERING
    val waitingConfirmation: StateFlow<List<Order>> = orderModel.WAITING_RESTAURANT_CONFIRMATION
    val confirmed: StateFlow<List<Order>> = orderModel.RESTAURANT_CONFIRMED
    val cancelled: StateFlow<List<Order>> = orderModel.DELIVERED
    val delivered: StateFlow<List<Order>> = orderModel.cancelled


    // Hàm gọi danh sách đơn hàng từ Model
    fun fetchOrders(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = orderModel.fetchAndCategorizeOrders()
                onResult(result) // Trả kết quả thành công/thất bại
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false) // Xử lý lỗi
            }
        }
    }

    fun updateOrderStatus(order: Order, newStatus: OrderStatus, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = orderModel.updateOrderStatus(order, newStatus)
                if (result) {
                    when (newStatus) {
                        OrderStatus.CANCELLED -> orderModel.rejectOrder(order)
                        OrderStatus.RESTAURANT_CONFIRMED -> orderModel.confirmOrder(order)
                        else -> {}
                    }
                }
                onResult(result)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
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

    fun cancelOrderWithReason(orderId: Int, reason: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val order = confirmed.value.find { it.orderId == orderId }
                if (order != null) {
                    val updated = order.copy(orderStatus = OrderStatus.CANCELLED)
                    val result = orderModel.updateOrderStatus(updated, OrderStatus.CANCELLED)
                    if (result) {
                        orderModel.rejectOrder(order)
                        println("Hủy đơn hàng #$orderId với lý do: $reason")
                    }
                    onResult(result)
                } else {
                    println("Không tìm thấy đơn hàng #$orderId để hủy.")
                    onResult(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

}



