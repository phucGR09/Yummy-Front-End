package com.example.yummy.models

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)