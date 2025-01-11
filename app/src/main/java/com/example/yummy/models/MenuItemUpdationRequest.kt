package com.example.yummy.models

import java.math.BigDecimal

data class MenuItemUpdationRequest(
     val id: Int,
     val name: String,
     val price: BigDecimal,
     val description: String,
     val imageUrl: String,
     val restaurantId: Int,
     val restaurant: Restaurant,
)