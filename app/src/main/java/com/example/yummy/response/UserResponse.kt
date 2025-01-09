package com.example.yummy.response

data class UserResponse(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val userType: String
)