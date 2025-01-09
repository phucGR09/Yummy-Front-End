package com.example.yummy.models

data class RestaurantCreationRequest(
    val name: String,
    val address: String,
    val ownerUsername: String
)
