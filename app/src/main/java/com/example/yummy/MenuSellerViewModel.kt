package com.example.yummy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val REQUEST_CODE_PICK_IMAGE = 1001


data class Dish(
    val name: String,
    val price: Int,
    val description: String?, // Mô tả món ăn (tùy chọn)
    var imagePath: String? // Đường dẫn đến ảnh món ăn
)

class MenuSellerViewModel : ViewModel() {
    private val _dishes = MutableStateFlow(
        listOf(
            Dish(name = "Latte", price = 50000, description = "Latte Việt Nam", imagePath = null),
            Dish(name = "Bánh tráng trộn", price = 25000, description = null, imagePath = null),
            Dish(name = "Matcha latte", price = 55000, description = "Matcha latte rất ngon", imagePath = null)
        )
    )
    val dishes: StateFlow<List<Dish>> = _dishes

    // Thêm món ăn mới
    fun addDish(newDish: Dish) {
        val updatedDishes = _dishes.value.toMutableList()
        updatedDishes.add(newDish)
        _dishes.value = updatedDishes
    }

    // Cập nhật món ăn
    fun updateDish(updatedDish: Dish) {
        val updatedDishes = _dishes.value.map { dish ->
            if (dish.name == updatedDish.name) updatedDish else dish
        }
        _dishes.value = updatedDishes
    }

    // Xóa món ăn
    fun removeDish(dishName: String) {
        val updatedDishes = _dishes.value.filter { it.name != dishName }
        _dishes.value = updatedDishes
    }

    fun updateImage(dish: Dish, imagePath: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Giả lập gọi API upload ảnh
                val uploadedUrl = uploadImageToServer(imagePath)

                // Cập nhật đường dẫn ảnh mới
                val updatedDish = dish.copy(imagePath = uploadedUrl)
                updateDish(updatedDish)

                onResult(true) // Thành công
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false) // Thất bại
            }
        }
    }

    private suspend fun uploadImageToServer(imagePath: String): String {
        // Thay bằng logic thực tế để upload ảnh lên server
        delay(1000) // Giả lập thời gian tải lên
        return "https://example.com/images/${imagePath.hashCode()}.jpg" // URL giả lập
    }
}
