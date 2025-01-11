package com.example.yummy.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yummy.api.AuthenticationApi
import com.example.yummy.models.*
import com.example.yummy.response.AuthenticateResponse
import com.example.yummy.response.CustomerResponse
import com.example.yummy.response.DeliveryDriverResponse
import com.example.yummy.response.RestaurantOwnerResponse
import com.example.yummy.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationViewModel(private val authenticationApi: AuthenticationApi) : ViewModel() {

    // LiveData để theo dõi kết quả của các API request
    private val _registerResult = MutableStateFlow<ApiResponse<UserResponse>?>(null)
    val registerResult: StateFlow<ApiResponse<UserResponse>?> = _registerResult

    private val _loginResult = MutableStateFlow<ApiResponse<AuthenticateResponse>?>(null)
    val loginResult: StateFlow<ApiResponse<AuthenticateResponse>?> = _loginResult
    val createCustomerResult = mutableStateOf<ApiResponse<CustomerResponse>?>(null)
    val createDriverResult = mutableStateOf<ApiResponse<DeliveryDriverResponse>?>(null)
    val createRestaurantOwnerResult = mutableStateOf<ApiResponse<RestaurantOwnerResponse>?>(null)

    // Đăng ký người dùng mới
    fun registerUser(request: RegisterRequest) {
        authenticationApi.register(request).enqueue(object : Callback<ApiResponse<UserResponse>> {
            override fun onResponse(call: Call<ApiResponse<UserResponse>>, response: Response<ApiResponse<UserResponse>>) {
                if (response.isSuccessful) {
                    _registerResult.value = response.body()
                } else {
                    Log.e("Authentication", "Register Failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<UserResponse>>, t: Throwable) {
                Log.e("Authentication", "Register Failed: ${t.message}")
            }
        })
    }

    // Đăng nhập
    fun loginUser(request: AuthenticateRequest) {
        authenticationApi.login(request).enqueue(object : Callback<ApiResponse<AuthenticateResponse>> {
            override fun onResponse(call: Call<ApiResponse<AuthenticateResponse>>, response: Response<ApiResponse<AuthenticateResponse>>) {
                if (response.isSuccessful) {
                    _loginResult.value = response.body()
                } else {
                    Log.e("Authentication", "Login Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<AuthenticateResponse>>, t: Throwable) {
                Log.e("Authentication", "Login Failed: ${t.message}")
            }
        })
    }

    // Tạo khách hàng
    fun createCustomer(request: CustomerCreationRequest) {
        authenticationApi.createCustomer(request).enqueue(object : Callback<ApiResponse<CustomerResponse>> {
            override fun onResponse(call: Call<ApiResponse<CustomerResponse>>, response: Response<ApiResponse<CustomerResponse>>) {
                if (response.isSuccessful) {
                    createCustomerResult.value = response.body()
                } else {
                    Log.e("Authentication", "Create Customer Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<CustomerResponse>>, t: Throwable) {
                Log.e("Authentication", "Create Customer Failed: ${t.message}")
            }
        })
    }

    // Tạo người lái xe giao hàng
    fun createDeliveryDriver(request: DeliveryDriverCreationRequest) {
        authenticationApi.createDeliveryDriver(request).enqueue(object : Callback<ApiResponse<DeliveryDriverResponse>> {
            override fun onResponse(call: Call<ApiResponse<DeliveryDriverResponse>>, response: Response<ApiResponse<DeliveryDriverResponse>>) {
                if (response.isSuccessful) {
                    createDriverResult.value = response.body()
                } else {
                    Log.e("Authentication", "Create Driver Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<DeliveryDriverResponse>>, t: Throwable) {
                Log.e("Authentication", "Create Driver Failed: ${t.message}")
            }
        })
    }

    // Tạo chủ nhà hàng
    fun createRestaurantOwner(request: RestaurantOwnerCreationRequest) {
        authenticationApi.createRestaurantOwner(request).enqueue(object : Callback<ApiResponse<RestaurantOwnerResponse>> {
            override fun onResponse(call: Call<ApiResponse<RestaurantOwnerResponse>>, response: Response<ApiResponse<RestaurantOwnerResponse>>) {
                if (response.isSuccessful) {
                    createRestaurantOwnerResult.value = response.body()
                } else {
                    Log.e("Authentication", "Create Restaurant Owner Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<RestaurantOwnerResponse>>, t: Throwable) {
                Log.e("Authentication", "Create Restaurant Owner Failed: ${t.message}")
            }
        })
    }
}
