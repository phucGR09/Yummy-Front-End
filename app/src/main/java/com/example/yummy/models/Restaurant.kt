package com.example.yummy.models

import java.time.LocalTime

data class Restaurant(
    val id: Int,
    val name: String,
    val address: String,
    val openingHours: LocalTime,
    val owner: RestaurantOwner,
    val menuItems: List<MenuItem>
)