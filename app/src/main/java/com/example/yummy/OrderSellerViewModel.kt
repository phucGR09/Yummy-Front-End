package com.example.yummy

import java.time.LocalDateTime

data class Order(
    val orderId: Int, // Primary Key
    val restaurantId: Int, // Foreign Key (Restaurant)
    val driverId: Int, // Foreign Key (Driver)
    val customerId: Int, // Foreign Key (Customer)
    val orderTime: LocalDateTime, // Thời gian đặt hàng
    val totalPrice: Double, // Tổng tiền đơn hàng
    val orderStatus: OrderStatus // Trạng thái đơn hàng
)

// Enum class cho trạng thái đơn hàng
enum class OrderStatus {
    waiting_confirmation, // Đơn hàng đang chờ xử lý
    confirmed, // Đơn hàng đang được chuẩn bị
    waiting_delivery, // Đơn hàng đã hoàn thành
    delivered,
    cancelled// Đơn hàng đã bị hủy
}
