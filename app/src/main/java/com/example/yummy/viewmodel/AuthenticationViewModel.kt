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

    fun resetRegisterResult() {
        _registerResult.value = null
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun registerUser(
        request: RegisterRequest,
        onSuccess: (UserType) -> Unit,
        onFailure: (String) -> Unit
    ) {
        authenticationApi.register(request).enqueue(object : retrofit2.Callback<ApiResponse<UserResponse>> {
            override fun onResponse(
                call: retrofit2.Call<ApiResponse<UserResponse>>,
                response: retrofit2.Response<ApiResponse<UserResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.code == 200 && apiResponse.result != null) {
                        val userType = UserType.valueOf(request.userType.uppercase())
                        onSuccess(userType) // Gửi loại vai trò user về
                    } else {
                        onFailure(apiResponse.message ?: "Unknown error occurred")
                    }
                } else {
                    onFailure("Register failed with code: ${response.code()} and message: ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<ApiResponse<UserResponse>>, t: Throwable) {
                onFailure("Failed to connect to the server: ${t.message}")
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
    fun completeCustomer(request: YeuCauHoanThanhKhachHang, onResult: (ApiResponse<KhachHangResult>) -> Unit) {
        authenticationApi.makeCustomer(request).enqueue(object : Callback<ApiResponse<KhachHangResult>> {
            override fun onResponse(
                call: Call<ApiResponse<KhachHangResult>>,
                response: Response<ApiResponse<KhachHangResult>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("API_SUCCESS", "Response: $apiResponse")
                    onResult(apiResponse ?: ApiResponse(code = 500, message = "Response body is null"))
                } else {
                    Log.e("API_ERROR", "Error Code: ${response.code()}, Message: ${response.errorBody()?.string()}")
                    onResult(ApiResponse(code = response.code(), message = response.message()))
                }
            }

            override fun onFailure(call: Call<ApiResponse<KhachHangResult>>, t: Throwable) {
                Log.e("API_FAILURE", "Error: ${t.localizedMessage}")
                onResult(ApiResponse(code = 500, message = t.localizedMessage ?: "Unknown Error"))
            }
        })
    }
    fun taoNhaHang(request: YeuCauTaoNhaHang, onResult: (ApiResponse<ChiTietNhaHang>) -> Unit) {
        authenticationApi.makeRestaurant(request).enqueue(object : Callback<ApiResponse<ChiTietNhaHang>> {
            override fun onResponse(call: Call<ApiResponse<ChiTietNhaHang>>, response: Response<ApiResponse<ChiTietNhaHang>>) {
                if (response.isSuccessful) {
                    onResult(response.body() ?: ApiResponse(code = 500, message = "Response body is null"))
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    onResult(ApiResponse(code = response.code(), message = errorMessage))
                }
            }

            override fun onFailure(call: Call<ApiResponse<ChiTietNhaHang>>, t: Throwable) {
                onResult(ApiResponse(code = 500, message = t.localizedMessage ?: "Unknown error"))
            }
        })
    }



}
