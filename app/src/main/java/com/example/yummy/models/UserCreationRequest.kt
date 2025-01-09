package com.example.yummy.models

data class UserCreationRequest(
    val userName: String,
    val password: String,
    val email: String,
    val fullName: String?,
    val phoneNumber: String?,
    val userType: String
)
