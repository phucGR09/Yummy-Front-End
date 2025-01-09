package com.example.yummy.models

data class RestaurantResponse(
    val id: Int,
    val name: String,
    val address: String,
    val ownerUsername: String
)
