package com.example.yummy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.delay

// Data class đại diện cho một món ăn
data class Dish(
    val itemId: Int, // ID của món ăn
    val restaurantId: Int, // ID nhà hàng
    val name: String, // Tên món ăn
    val price: Int, // Giá món ăn
    val description: String?, // Mô tả món ăn (tùy chọn)
    var imagePath: String? // Đường dẫn ảnh món ăn
)

class MenuModel {
    // Danh sách món ăn được quản lý bằng StateFlow
    private val _dishes = MutableStateFlow<List<Dish>>(
        listOf(
            Dish(
                itemId = 1,
                restaurantId = 1,
                name = "Bánh Tráng Trộn",
                price = 25000,
                description = "Món ăn vặt truyền thống với đủ hương vị",
                imagePath = null
            ),
            Dish(
                itemId = 2,
                restaurantId = 1,
                name = "Matcha Latte",
                price = 55000,
                description = "Thức uống thơm ngon với vị matcha đậm đà",
                imagePath = null
            ),
            Dish(
                itemId = 3,
                restaurantId = 1,
                name = "Latte",
                price = 50000,
                description = "Thức uống cà phê truyền thống với vị béo nhẹ",
                imagePath = null
            )
        )
    )
    val dishes: StateFlow<List<Dish>> = _dishes

    // Thêm món ăn mới
    suspend fun addDish(newDish: Dish): Boolean {
        return try {
            val serverDish = addDishOnServer(newDish)
            _dishes.value = _dishes.value + serverDish
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Cập nhật món ăn
    suspend fun updateDish(updatedDish: Dish): Boolean {
        return try {
            val serverDish = updateDishOnServer(updatedDish)
            _dishes.value = _dishes.value.map { dish ->
                if (dish.itemId == serverDish.itemId) serverDish else dish
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Xóa món ăn theo ID
    suspend fun removeDishById(dishId: Int): Boolean {
        return try {
            removeDishOnServer(dishId)
            _dishes.value = _dishes.value.filter { it.itemId != dishId }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Xóa món ăn theo tên
    suspend fun removeDishByName(dishName: String): Boolean {
        return try {
            // Tìm món ăn cần xóa
            val dishToRemove = _dishes.value.find { it.name == dishName }
                ?: throw Exception("Món ăn không tồn tại")

            // Gọi server để xóa món ăn
            removeDishByNameOnServer(dishToRemove)

            // Cập nhật danh sách món ăn cục bộ
            _dishes.value = _dishes.value.filter { it.name != dishName }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Cập nhật đường dẫn ảnh cho món ăn
    suspend fun updateImage(dish: Dish, imagePath: String) {
        val updatedDish = dish.copy(imagePath = imagePath)
        updateDish(updatedDish)
    }
    // Giả lập thêm món ăn trên server
    private suspend fun addDishOnServer(dish: Dish): Dish {
        delay(1000)
        println("Món ăn đã được thêm trên server: $dish")
        return dish.copy(itemId = (1..1000).random())
    }

    // Giả lập cập nhật món ăn trên server
    private suspend fun updateDishOnServer(dish: Dish): Dish {
        delay(1000)
        println("Món ăn đã được cập nhật trên server: $dish")
        return dish
    }

    // Giả lập xóa món ăn theo ID trên server
    private suspend fun removeDishOnServer(dishId: Int) {
        delay(1000)
        println("Món ăn với ID $dishId đã được xóa trên server")
    }

    // Giả lập xóa món ăn theo tên trên server
    private suspend fun removeDishByNameOnServer(dish: Dish) {
        delay(1000)
        println("Món ăn ${dish.name} đã được xóa trên server")
    }



}
