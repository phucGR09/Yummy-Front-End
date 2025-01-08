package com.example.yummy

data class Category(
    val name: String,
    val dishes: List<Dish> // Danh sách món ăn thuộc danh mục này
)

data class Dish(
    val name: String,
    val price: Int,
    val description: String?, // Mô tả món ăn (tùy chọn, có thể null)
    val category: String // Tên danh mục của món
)


class MenuSellerViewModel {

}