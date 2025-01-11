package com.example.yummy.api

import com.example.yummy.models.*
import com.example.yummy.response.AuthenticateResponse
import com.example.yummy.response.CustomerResponse
import com.example.yummy.response.DeliveryDriverResponse
import com.example.yummy.response.RestaurantOwnerResponse
import com.example.yummy.response.UserResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("api/v1/auth/register")
    fun register(@Body request: RegisterRequest): Call<ApiResponse<UserResponse>>

    @POST("api/v1/auth/complete/delivery-driver")
    fun createDeliveryDriver(@Body request: DeliveryDriverCreationRequest): Call<ApiResponse<DeliveryDriverResponse>>

    @POST("api/v1/auth/complete/customer")
    fun createCustomer(@Body request: CustomerCreationRequest): Call<ApiResponse<CustomerResponse>>

    @POST("api/v1/auth/complete/restaurant-owner")
    fun createRestaurantOwner(@Body request: RestaurantOwnerCreationRequest): Call<ApiResponse<RestaurantOwnerResponse>>

    @POST("api/v1/auth/login")
    fun login(@Body request: AuthenticateRequest): Call<ApiResponse<AuthenticateResponse>>
    companion object {
        private const val BASE_URL = "http://192.168.229.110:8080/" // Replace with your API base URL

        fun create(): AuthenticationApi {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(AuthenticationApi::class.java)
        }
    }
}
