package com.example.yummy

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call // Đây là lớp cơ bản để định nghĩa các API call trong Retrofit
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE

data class CreateMenuItemRequest(
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String,
    val restaurantId: Int
)

data class CreateMenuItemResponse(
    val code: Int,
    val message: String,
    val result: MenuItemResult
)

data class MenuItemResult(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String,
    val restaurant: RestaurantDetails
)

data class RestaurantDetails(
    val id: Int,
    val name: String,
    val address: String,
    val openingHours: OpeningHours,
    val owner: OwnerDetails
)

data class OpeningHours(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
)

data class OwnerDetails(
    val id: Int,
    val taxNumber: String,
    val user: UserDetails
)

data class UserDetails(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val userType: String
)

interface ApiService {
    @POST("admin/menu-items/create")
    suspend fun addDish(@Body dish: Dish): Dish

    @PUT("admin/menu-items/update/{id}")
    suspend fun updateDish(@Path("id") id: Int, @Body dish: Dish): Dish

    @DELETE("admin/menu-items/delete/{id}")
    suspend fun deleteDish(@Path("id") id: Int)
}
