package com.example.yummy.response


data class AuthenticateResponse(
    val token: String,
    val userType: String
)
