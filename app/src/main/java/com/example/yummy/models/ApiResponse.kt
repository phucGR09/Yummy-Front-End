package com.example.yummy.models

data class ApiResponse<T>(
    val code: Int = 200, // 200: Success
    val message: String? = null,
    val result: T? = null
)
