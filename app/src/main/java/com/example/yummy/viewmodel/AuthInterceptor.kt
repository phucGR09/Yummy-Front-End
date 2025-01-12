package com.example.yummy.viewmodel

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import com.example.yummy.AuthManager

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Lấy token từ AuthManager
        val token = AuthManager.token
        Log.d("AuthInterceptor", "Token retrieved: $token")
        // Nếu có token thì thêm vào header Authorization
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
                Log.d("AuthInterceptor", "add token: success!")
            }
            else {
                Log.d("AuthInterceptor", "Token is null or empty") // Log khi token không hợp lệ
            }
        }.build()

        return chain.proceed(request)
    }
}
