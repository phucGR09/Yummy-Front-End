package com.example.yummy.response

import com.example.yummy.models.User

data class AuthenticateResponse(
    val token: String,
    val user: User
)
