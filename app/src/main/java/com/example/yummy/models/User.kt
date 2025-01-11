package com.example.yummy.models

data class User(
    val id: Int?,
    val userName: String,
    val email: String,
    val fullName: String?,
    val phoneNumber: String?,
    val userType: UserType
)

