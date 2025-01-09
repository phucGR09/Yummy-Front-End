package com.example.yummy

data class Dish(
    val item_id: Int,
    val restaurant_id: Int,
    val name: String,
    val price: Int,
    val description: String?, // Mô tả món ăn (tùy chọn)
    var imagePath: String? // Đường dẫn đến ảnh món ăn
)