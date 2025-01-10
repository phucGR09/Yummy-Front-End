package com.example.yummy

import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://192.168.1.171:8080/api/v1/"  // Thay bằng địa chỉ IP backend

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var authToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJZdW1teS1iYWNrZW5kLXRlYW0iLCJzdWIiOiJmb28iLCJleHAiOjE3MzY1NzI4MjcsImlhdCI6MTczNjQ4NjQyNywic2NvcGUiOiJBRE1JTiJ9.Sc-OtN4nM0Q8RYYNTCdNNtYDoMmi2BxG04oedBdt-JqNc0m_G21xKJ1OnPMWM2BRNLbqrciLgPLyd45t3iaV6w"

    fun setAuthToken(token: String) {
        authToken = token
    }

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        // Thêm header Authorization với token Bearer
        requestBuilder.addHeader("Authorization", "Bearer $authToken")

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
