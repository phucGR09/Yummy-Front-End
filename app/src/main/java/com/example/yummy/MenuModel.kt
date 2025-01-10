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

    private val apiService = ApiClient.instance.create(ApiService::class.java)

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes

    // Thêm món ăn mới
    suspend fun addDish(newDish: Dish): Boolean {
        return try {
            // Kiểm tra món ăn trùng tên
            if (_dishes.value.any { it.name == newDish.name }) {
                println("Món ăn đã tồn tại, không thể thêm mới")
                return false
            }

            // Tạo yêu cầu thêm món ăn
            val request = CreateMenuItemRequest(
                name = newDish.name,
                price = newDish.price.toDouble(),
                description = newDish.description.orEmpty(),
                imageUrl = newDish.imagePath.orEmpty(),
                restaurantId = newDish.restaurantId
            )

            val response = apiService.createMenuItem(request)

            // Kiểm tra phản hồi từ server
            if (response.code == 200) {
                val createdDish = newDish.copy(
                    itemId = response.result.id,
                    restaurantId = response.result.restaurant.id
                )
                _dishes.value = _dishes.value + createdDish
                println("Thêm món ăn thành công")
                true
            } else {
                println("Thêm món ăn thất bại: ${response.message}")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Cập nhật món ăn
    suspend fun updateDish(updatedDish: Dish): Boolean {
        return try {
            val request = UpdateMenuItemRequest(
                id = updatedDish.itemId,
                name = updatedDish.name,
                price = updatedDish.price.toDouble(),
                description = updatedDish.description.orEmpty(),
                imageUrl = updatedDish.imagePath.orEmpty(),
                restaurantId = updatedDish.restaurantId
            )

            val response = apiService.updateMenuItem(request)

            // Kiểm tra phản hồi từ server
            if (response.code == 200) {
                _dishes.value = _dishes.value.map { dish ->
                    if (dish.itemId == response.result.id) {
                        updatedDish.copy(
                            itemId = response.result.id,
                            restaurantId = response.result.restaurant.id
                        )
                    } else dish
                }
                println("Cập nhật món ăn thành công")
                true
            } else {
                println("Cập nhật món ăn thất bại: ${response.message}")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Xóa món ăn
    suspend fun removeDishById(dishId: Int): Boolean {
        return try {
            val response = apiService.deleteDish(dishId)
            if (response.code() == 200) {
                _dishes.value = _dishes.value.filter { it.itemId != dishId }
                println("Xóa món ăn thành công")
                true
            } else {
                println("Xóa món ăn thất bại: ${response.message()}")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Lấy danh sách món ăn
    suspend fun getDishes(): Boolean {
        return try {
            val response = apiService.getMenu()
            if (response.code == 200) {
                val dishesFromServer = response.result.map { menuItem ->
                    Dish(
                        itemId = menuItem.id,
                        restaurantId = menuItem.restaurant.id,
                        name = menuItem.name,
                        price = menuItem.price.toInt(),
                        description = menuItem.description,
                        imagePath = menuItem.imageUrl
                    )
                }
                _dishes.value = dishesFromServer
                println("Lấy danh sách món ăn thành công")
                true
            } else {
                println("Lấy danh sách món ăn thất bại: ${response.message}")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
