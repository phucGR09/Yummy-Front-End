package com.example.yummy.response

import com.example.yummy.models.Restaurant
import java.math.BigDecimal

data class MenuItemResponse(
    val id: Int,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val imageUrl: String,
    val restaurantId: Int,
    val restaurant: Restaurant,
)