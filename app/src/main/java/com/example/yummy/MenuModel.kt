package com.example.yummy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Data class đại diện cho một món ăn
data class Dish(
    val itemId: Int, // ID của món ăn
    val restaurantId: Int, // ID nhà hàng
    val name: String, // Tên món ăn
    val price: Int, // Giá món ăn
    val description: String?, // Mô tả món ăn (tùy chọn)
    var imagePath: String? // Đường dẫn ảnh món ăn
)

object SharedData {
    var restaurantId: Int = 1
    var userToken: String? = null
}

class MenuModel {

    private val apiService = ApiClient.instance.create(ApiService::class.java)

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes

    private val _restaurants = MutableStateFlow<List<RestaurantDetails>>(emptyList())
    val restaurants: StateFlow<List<RestaurantDetails>> = _restaurants

    suspend fun fetchRestaurants(username: String): List<RestaurantDetails>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRestaurantsByUsername(username)
                if (response.isSuccessful) {
                    val restaurants = response.body()?.result.orEmpty()
                    _restaurants.value = restaurants
                    return@withContext restaurants
                } else {
                    println("Error fetching restaurants: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getDishesByRestaurantId(restaurantId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMenuItemByRestaurantId(restaurantId)
                if (response.isSuccessful) {
                    val dishesFromServer = response.body()?.result?.map { menuItem ->
                        Dish(
                            itemId = menuItem.id,
                            restaurantId = menuItem.restaurant.id,
                            name = menuItem.name,
                            price = menuItem.price.toInt(),
                            description = menuItem.description,
                            imagePath = menuItem.imageUrl
                        )
                    }.orEmpty()
                    _dishes.value = dishesFromServer
                    true
                } else {
                    println("Error fetching dishes: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    // Thêm món ăn mới
    suspend fun addDish(newDish: Dish): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (_dishes.value.any { it.name == newDish.name }) {
                    println("Món ăn đã tồn tại, không thể thêm mới")
                    return@withContext false
                }

                val request = CreateMenuItemRequest(
                    name = newDish.name,
                    price = newDish.price.toDouble(),
                    description = newDish.description.orEmpty(),
                    imageUrl = newDish.imagePath.orEmpty(),
                    restaurantId = newDish.restaurantId
                )

                val response = apiService.createMenuItem(request)
                if (response.isSuccessful) {
                    val result = response.body()?.result ?: return@withContext false
                    val createdDish = newDish.copy(
                        itemId = result.id,
                        restaurantId = result.restaurant.id
                    )
                    _dishes.value = _dishes.value + createdDish
                    println("Thêm món ăn thành công")
                    return@withContext true
                } else {
                    println("Thêm món ăn thất bại: ${response.message()}")
                    return@withContext false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    // Cập nhật món ăn
    suspend fun updateDish(updatedDish: Dish): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = UpdateMenuItemRequest(
                    id = updatedDish.itemId,
                    name = updatedDish.name,
                    price = updatedDish.price.toDouble(),
                    description = updatedDish.description.orEmpty(),
                    imageUrl = updatedDish.imagePath.orEmpty(),
                    restaurantId = updatedDish.restaurantId
                )

                val response = apiService.updateMenuItem(request)
                if (response.isSuccessful) {
                    val result = response.body()?.result ?: return@withContext false
                    _dishes.value = _dishes.value.map { dish ->
                        if (dish.itemId == result.id) {
                            updatedDish.copy(
                                itemId = result.id,
                                restaurantId = result.restaurant.id
                            )
                        } else dish
                    }
                    println("Cập nhật món ăn thành công")
                    return@withContext true
                } else {
                    println("Cập nhật món ăn thất bại: ${response.message()}")
                    return@withContext false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    // Xóa món ăn
    suspend fun removeDishById(dishId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteDish(dishId)
                if (response.isSuccessful) {
                    _dishes.value = _dishes.value.filter { it.itemId != dishId }
                    println("Xóa món ăn thành công")
                    return@withContext true
                } else {
                    println("Xóa món ăn thất bại: ${response.errorBody()?.string()}")
                    return@withContext false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    suspend fun removeDishByName(dishName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val dishToRemove = _dishes.value.find { it.name == dishName }
                    ?: throw Exception("Món ăn không tồn tại")

                val response = apiService.deleteDish(dishToRemove.itemId)
                if (response.isSuccessful) {
                    _dishes.value = _dishes.value.filter { it.itemId != dishToRemove.itemId }
                    println("Xóa món ăn thành công")
                    return@withContext true
                } else {
                    println("Xóa món ăn thất bại: ${response.errorBody()?.string()}")
                    return@withContext false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    // Lấy danh sách món ăn
    suspend fun getDishes(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMenu()
                if (response.isSuccessful) {
                    val dishesFromServer = response.body()?.result?.map { menuItem ->
                        Dish(
                            itemId = menuItem.id,
                            restaurantId = menuItem.restaurant.id,
                            name = menuItem.name,
                            price = menuItem.price.toInt(),
                            description = menuItem.description,
                            imagePath = menuItem.imageUrl
                        )
                    }.orEmpty()
                    _dishes.value = dishesFromServer
                    println("Lấy danh sách món ăn thành công")
                    return@withContext true
                } else {
                    println("Lấy danh sách món ăn thất bại: ${response.message()}")
                    return@withContext false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }

    // Cập nhật ảnh món ăn
    suspend fun updateImage(dish: Dish, uploadedUrl: String){

    }

    suspend fun searchDishByName(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMenuItemsByContainedDishName(name)
                if (response.isSuccessful) {
                    val dishesFromServer = response.body()?.result?.map { menuItem ->
                        Dish(
                            itemId = menuItem.id,
                            restaurantId = menuItem.restaurant.id,
                            name = menuItem.name,
                            price = menuItem.price.toInt(),
                            description = menuItem.description,
                            imagePath = menuItem.imageUrl
                        )
                    }.orEmpty()
                    _dishes.value = dishesFromServer
                    println("Tìm kiếm món ăn thành công")
                    return@withContext true
                } else {
                    println("Tìm kiếm món ăn thất bại: ${response.message()}")
                    return@withContext false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
    }
}


