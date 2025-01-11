package com.example.yummy.viewmodel

import okhttp3.Interceptor
import okhttp3.Response
import com.example.yummy.AuthManager

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Lấy token từ AuthManager
        val token = AuthManager.token

        // Nếu có token thì thêm vào header Authorization
        val request = chain.request().newBuilder().apply {
            token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(request)
    }
}
