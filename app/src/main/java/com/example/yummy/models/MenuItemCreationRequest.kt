package com.example.yummy.models

import android.icu.math.BigDecimal

data class MenuItemCreationRequest(
    val name: String,
    val price: BigDecimal,
    val description: String,
    val imageUrl: String,
    val restaurantId: Int,
    val restaurant: Restaurant,
)