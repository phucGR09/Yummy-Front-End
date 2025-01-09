package com.example.yummy.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yummy.api.UserApi
import com.example.yummy.models.ApiResponse
import com.example.yummy.models.User
import com.example.yummy.models.UserCreationRequest
import com.example.yummy.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    // LiveData to observe API responses
    private val _userResponse = MutableLiveData<ApiResponse<List<User>>>()
    val userResponse: LiveData<ApiResponse<List<User>>> get() = _userResponse

    private val _singleUserResponse = MutableLiveData<ApiResponse<User>>()
    val singleUserResponse: LiveData<ApiResponse<User>> get() = _singleUserResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Function to fetch all users from the API
    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val userApi: UserApi = RetrofitInstance.userApi

            userApi.getAllUsers().enqueue(object : Callback<ApiResponse<List<User>>> {
                override fun onResponse(call: Call<ApiResponse<List<User>>>, response: Response<ApiResponse<List<User>>>) {
                    if (response.isSuccessful) {
                        _userResponse.postValue(response.body())
                    } else {
                        _errorMessage.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<List<User>>>, t: Throwable) {
                    _errorMessage.postValue("Failure: ${t.localizedMessage}")
                    Log.e("UserViewModel", "Error fetching users: ${t.localizedMessage}")
                }
            })
        }
    }

    // Function to create a new user
    fun createUser(userCreationRequest: UserCreationRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val userApi: UserApi = RetrofitInstance.userApi

            userApi.createUser(userCreationRequest).enqueue(object : Callback<ApiResponse<User>> {
                override fun onResponse(call: Call<ApiResponse<User>>, response: Response<ApiResponse<User>>) {
                    if (response.isSuccessful) {
                        _singleUserResponse.postValue(response.body())
                    } else {
                        _errorMessage.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                    _errorMessage.postValue("Failure: ${t.localizedMessage}")
                    Log.e("UserViewModel", "Error creating user: ${t.localizedMessage}")
                }
            })
        }
    }

    // Function to fetch a user by username
    fun getUserByUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userApi: UserApi = RetrofitInstance.userApi

            userApi.getUserByUsername(username).enqueue(object : Callback<ApiResponse<User>> {
                override fun onResponse(call: Call<ApiResponse<User>>, response: Response<ApiResponse<User>>) {
                    if (response.isSuccessful) {
                        _singleUserResponse.postValue(response.body())
                    } else {
                        _errorMessage.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                    _errorMessage.postValue("Failure: ${t.localizedMessage}")
                    Log.e("UserViewModel", "Error fetching user by username: ${t.localizedMessage}")
                }
            })
        }
    }
}
