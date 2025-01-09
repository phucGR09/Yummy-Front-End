package com.example.yummy.repository

import com.example.yummy.API.UserApi
import com.example.yummy.models.User
import com.example.yummy.models.UserCreationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val userApi: UserApi) {

    // Lấy tất cả người dùng
    fun getAllUsers(callback: (List<User>) -> Unit) {
        userApi.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    response.body()?.let { users ->
                        callback(users)
                    }
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    // Tạo người dùng mới
    fun createUser(userCreationRequest: UserCreationRequest, callback: (User) -> Unit) {
        userApi.createUser(userCreationRequest).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        callback(user)
                    }
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle failure
            }
        })
    }

    // Các phương thức khác, ví dụ như lấy user theo username, v.v.
}
