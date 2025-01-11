package com.example.yummy.network

import com.example.yummy.api.UserApi
import com.example.yummy.api.RestaurantApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Địa chỉ của API (thay đổi phù hợp với backend của bạn)
    private const val BASE_URL = "http://192.168.1.3:8080/"

    // Tạo Retrofit instance
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)  // Cấu hình base URL cho API
            .addConverterFactory(GsonConverterFactory.create())  // Converter để chuyển đổi JSON thành đối tượng Kotlin
            .client(OkHttpClient())  // Tùy chọn: Cấu hình OkHttpClient nếu cần
            .build()
    }
    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
    val restaurantApi: RestaurantApi by lazy {
        retrofit.create(RestaurantApi::class.java)
    }
}
