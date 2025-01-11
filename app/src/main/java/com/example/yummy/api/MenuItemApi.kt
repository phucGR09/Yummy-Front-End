package com.example.yummy.api

import com.example.yummy.models.MenuItemCreationRequest
import com.example.yummy.models.MenuItemUpdationRequest
import com.example.yummy.models.ApiResponse
import com.example.yummy.response.MenuItemResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface MenuItemApi {

    // Endpoint to get all menu items
    @GET("api/v1/admin/menu-items")
    fun getAllMenuItems(): Call<ApiResponse<List<MenuItemResponse>>>

    // Endpoint to get menu items by restaurant ID
    @GET("api/v1/admin/menu-items/restaurant-id")
    fun getMenuItemsByRestaurantId(@Query("id") restaurantId: Int): Call<ApiResponse<List<MenuItemResponse>>>

    // Endpoint to get menu items by contained dish name
    @GET("api/v1/admin/menu-items/contained-dish-name/name")
    fun getMenuItemsByContainedDishName(@Query("name") name: String): Call<ApiResponse<List<MenuItemResponse>>>

    // Endpoint to create a new menu item
    @POST("api/v1/admin/menu-items/create")
    fun createMenuItem(@Body request: MenuItemCreationRequest): Call<ApiResponse<MenuItemResponse>>

    // Endpoint to delete a menu item by ID
    @DELETE("api/v1/admin/menu-items/delete/id")
    fun deleteMenuItem(@Query("id") id: Int): Call<ApiResponse<Boolean>>

    // Endpoint to update a menu item
    @PATCH("api/v1/admin/menu-items/update")
    fun updateMenuItem(@Body request: MenuItemUpdationRequest): Call<ApiResponse<MenuItemResponse>>
}
