package com.example.yummy.network

import android.content.Context
import com.example.yummy.api.UserApi
import com.example.yummy.api.RestaurantApi
import com.example.yummy.viewmodel.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.229.110:8080/"

    private val retrofit by lazy {
        // Tạo OkHttpClient với AuthInterceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())  // Sử dụng AuthInterceptor để tự động thêm token vào header
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    // API services
    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val restaurantApi: RestaurantApi by lazy {
        retrofit.create(RestaurantApi::class.java)
    }
}
