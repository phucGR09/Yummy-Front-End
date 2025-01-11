package com.example.yummy

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call // Đây là lớp cơ bản để định nghĩa các API call trong Retrofit
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import java.time.LocalDateTime
import retrofit2.Response
import retrofit2.http.Query
import java.time.LocalTime


data class CreateMenuItemRequest(
    val name: String ="",
    val price: Double = 0.0,
    val description: String = "",
    val imageUrl: String = "",
    val restaurantId: Int
)

data class UpdateMenuItemRequest(
    val id: Int,
    val name: String,
    val price: Double= 0.0,
    val description: String ="",
    val imageUrl: String ="",
    val restaurantId: Int
)

data class UpdateMenuItemResponse(
    val code: Int,
    val message: String,
    val result: MenuItemResult
)


data class CreateMenuItemResponse(
    val code: Int,
    val message: String,
    val result: MenuItemResult
)

data class MenuItemsResponse(
    val code: Int,
    val message: String,
    val result: List<MenuItemResult>
)

data class MenuItemResult(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String= "",
    val imageUrl: String= "",
    val restaurant: RestaurantDetails
)

data class RestaurantDetails(
    val id: Int,
    val name: String,
    val address: String,
    val openingHours: String,
    val owner: OwnerDetails
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
    @PATCH("admin/menu-items/update")
    suspend fun updateMenuItem(@Body request: UpdateMenuItemRequest):  Response<UpdateMenuItemResponse>


    @DELETE("admin/menu-items/delete/id")
    suspend fun deleteDish(@Query("id") id: Int): Response<Unit>

    @GET("admin/menu-items")
    suspend fun getMenu(): Response<MenuItemsResponse>


    @POST("admin/menu-items/create")
    suspend fun createMenuItem(@Body request: CreateMenuItemRequest):  Response<CreateMenuItemResponse>

}
