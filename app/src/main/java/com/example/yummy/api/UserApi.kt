package com.example.yummy.api

import com.example.yummy.models.User
import com.example.yummy.models.ApiResponse
import com.example.yummy.models.UserCreationRequest
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    // Endpoint GET /api/v1/admin/users - Lấy danh sách tất cả người dùng
    @GET("api/v1/admin/users")
    fun getAllUsers(): Call<ApiResponse<List<User>>>

    // Endpoint POST /api/v1/admin/users/create - Tạo người dùng mới
    @POST("api/v1/admin/users/create")
    fun createUser(@Body user: UserCreationRequest): Call<ApiResponse<User>>

    // Endpoint GET /api/v1/admin/users/username - Lấy thông tin người dùng qua username
    @GET("api/v1/admin/users/username")
    fun getUserByUsername(@Query("username") username: String): Call<ApiResponse<User>>
}