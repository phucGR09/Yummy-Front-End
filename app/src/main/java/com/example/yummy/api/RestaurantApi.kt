package com.example.yummy.network.api

import com.example.yummy.models.ApiResponse
import com.example.yummy.models.RestaurantResponse
import com.example.yummy.models.RestaurantCreationRequest
import com.example.yummy.models.RestaurantUpdationRequest
import retrofit2.http.*

interface RestaurantApi {
    @GET("api/v1/admin/restaurants")
    suspend fun getAllRestaurants(): ApiResponse<List<RestaurantResponse>>

    @GET("api/v1/admin/restaurants/username")
    suspend fun getRestaurantByUsername(
        @Query("username") username: String
    ): ApiResponse<List<RestaurantResponse>>

    @GET("api/v1/admin/restaurants/id")
    suspend fun getRestaurantById(
        @Query("id") id: Int
    ): ApiResponse<RestaurantResponse>

    @POST("api/v1/admin/restaurants/create")
    suspend fun createRestaurant(
        @Body restaurantCreationRequest: RestaurantCreationRequest
    ): ApiResponse<RestaurantResponse>

    @PATCH("api/v1/admin/restaurants/update")
    suspend fun updateRestaurant(
        @Body restaurantUpdationRequest: RestaurantUpdationRequest
    ): ApiResponse<RestaurantResponse>

    @DELETE("api/v1/admin/restaurants/delete/id")
    suspend fun deleteRestaurant(
        @Query("id") id: Int
    ): ApiResponse<Boolean>
}
