package com.example.yummy.models

enum class UserType {
    CUSTOMER,
    RESTAURANT_OWNER,
    DELIVERY_DRIVER,
    ADMIN
}
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val userType: String // Thay đổi kiểu dữ liệu thành enum UserType
)