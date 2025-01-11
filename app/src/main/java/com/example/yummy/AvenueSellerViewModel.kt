package com.example.yummy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AvenueSellerViewModel(private val orderModel: OrderModel) {
    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue

    private val _totalCompletedOrders = MutableStateFlow(0)
    val totalCompletedOrders: StateFlow<Int> = _totalCompletedOrders

    private val _averageRevenuePerOrder = MutableStateFlow(0.0)
    val averageRevenuePerOrder: StateFlow<Double> = _averageRevenuePerOrder

    init {
        orderModel.getOrdersList(restaurantId = 1) // Truyền ID nhà hàng phù hợp

        updateMetrics()
    }

    // Hàm cập nhật các chỉ số
    fun updateMetrics() {
        _totalRevenue.update { orderModel.getTotalRevenue() }
        _totalCompletedOrders.update { orderModel.getTotalCompletedOrders() }
        _averageRevenuePerOrder.update { orderModel.getAverageRevenuePerOrder() }
    }
}
