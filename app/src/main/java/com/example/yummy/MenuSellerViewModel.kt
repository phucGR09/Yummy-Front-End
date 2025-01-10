package com.example.yummy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MenuSellerViewModel(private val menuModel: MenuModel) : ViewModel() {
    val dishes = menuModel.dishes

    // Gọi Model để thêm món ăn
    fun addDish(newDish: Dish, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = menuModel.addDish(newDish) // Gọi hàm suspend
                onResult(result) // Thành công
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false) // Thất bại
            }
        }
    }

    // Gọi Model để cập nhật món ăn
    fun updateDish(updatedDish: Dish, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = menuModel.updateDish(updatedDish) // Gọi hàm suspend
                onResult(result) // Thành công
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false) // Thất bại
            }
        }
    }

    // Gọi Model để xóa món ăn theo ID
    fun removeDishById(dishId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = menuModel.removeDishById(dishId) // Gọi hàm suspend
                onResult(result) // Thành công
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false) // Thất bại
            }
        }
    }

    // Gọi Model để xóa món ăn theo tên
    fun removeDishByName(dishName: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                menuModel.removeDishByName(dishName)
                onResult(true) // Thành công
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false) // Thất bại
            }
        }
    }

    // Cập nhật ảnh món ăn
    fun updateImage(dish: Dish, imagePath: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Giả lập gọi API upload ảnh
                val uploadedUrl = uploadImageToServer(imagePath)

                // Gọi Model để cập nhật ảnh
                menuModel.updateImage(dish, uploadedUrl)

                onResult(true) // Thành công
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false) // Thất bại
            }
        }
    }

    // Giả lập upload ảnh lên server
    private suspend fun uploadImageToServer(imagePath: String): String {
        // Thay thế bằng logic thực tế khi có API
        kotlinx.coroutines.delay(1000) // Giả lập thời gian tải lên
        return "https://example.com/images/${imagePath.hashCode()}.jpg" // URL giả lập
    }

//    fun rejectOrderWithReason(orderId: Int, reason: String, onResult: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            try {
//                val success = menuModel.rejectOrder(orderId, reason)
//                onResult(success)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                onResult(false) // Thất bại
//            }
//        }
//    }
}
