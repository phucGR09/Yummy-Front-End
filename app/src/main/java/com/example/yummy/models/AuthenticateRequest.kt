package com.example.yummy.models

data class AuthenticateRequest(
    val username: String,
    val password: String
)