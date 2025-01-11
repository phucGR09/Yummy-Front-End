package com.example.yummy

import android.content.Context

object AuthManager {
    var token: String? = null
    var userType: String? = null

    // Hàm để lưu token và userType vào SharedPreferences
    fun saveAuthData(context: Context, token: String, userType: String) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.putString("userType", userType)
        editor.apply()

        // Cập nhật vào object AuthManager
        this.token = token
        this.userType = userType
    }

    // Hàm để lấy token từ SharedPreferences
    fun getToken(context: Context): String? {
        // Truy xuất token từ SharedPreferences nếu có
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    // Hàm để lấy userType từ SharedPreferences
    fun getUserType(context: Context): String? {
        // Truy xuất userType từ SharedPreferences nếu có
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userType", null)
    }
}