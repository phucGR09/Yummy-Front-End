package com.example.yummy.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yummy.api.MenuItemApi
import com.example.yummy.models.ApiResponse
import com.example.yummy.models.MenuItemCreationRequest
import com.example.yummy.models.MenuItemUpdationRequest
import com.example.yummy.response.MenuItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuItemViewModel(private val menuItemApi: MenuItemApi) : ViewModel() {

    // StateFlow để theo dõi kết quả API
    private val _menuItems = MutableStateFlow<ApiResponse<List<MenuItemResponse>>?>(null)
    val menuItems: StateFlow<ApiResponse<List<MenuItemResponse>>?> = _menuItems

    private val _createMenuItemResult = mutableStateOf<ApiResponse<MenuItemResponse>?>(null)
    val createMenuItemResult = _createMenuItemResult

    private val _updateMenuItemResult = mutableStateOf<ApiResponse<MenuItemResponse>?>(null)
    val updateMenuItemResult = _updateMenuItemResult

    private val _deleteMenuItemResult = mutableStateOf<ApiResponse<Boolean>?>(null)
    val deleteMenuItemResult = _deleteMenuItemResult

    // Lấy tất cả món ăn
    fun getAllMenuItems() {
        menuItemApi.getAllMenuItems().enqueue(object : Callback<ApiResponse<List<MenuItemResponse>>> {
            override fun onResponse(call: Call<ApiResponse<List<MenuItemResponse>>>, response: Response<ApiResponse<List<MenuItemResponse>>>) {
                if (response.isSuccessful) {
                    _menuItems.value = response.body()
                } else {
                    Log.e("MenuItemViewModel", "Get All Menu Items Failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<MenuItemResponse>>>, t: Throwable) {
                Log.e("MenuItemViewModel", "Get All Menu Items Failed: ${t.message}")
            }
        })
    }

    // Tạo món ăn mới
    fun createMenuItem(request: MenuItemCreationRequest) {
        menuItemApi.createMenuItem(request).enqueue(object : Callback<ApiResponse<MenuItemResponse>> {
            override fun onResponse(call: Call<ApiResponse<MenuItemResponse>>, response: Response<ApiResponse<MenuItemResponse>>) {
                if (response.isSuccessful) {
                    _createMenuItemResult.value = response.body()
                } else {
                    Log.e("MenuItemViewModel", "Create Menu Item Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<MenuItemResponse>>, t: Throwable) {
                Log.e("MenuItemViewModel", "Create Menu Item Failed: ${t.message}")
            }
        })
    }

    // Cập nhật món ăn
    fun updateMenuItem(request: MenuItemUpdationRequest) {
        menuItemApi.updateMenuItem(request).enqueue(object : Callback<ApiResponse<MenuItemResponse>> {
            override fun onResponse(call: Call<ApiResponse<MenuItemResponse>>, response: Response<ApiResponse<MenuItemResponse>>) {
                if (response.isSuccessful) {
                    _updateMenuItemResult.value = response.body()
                } else {
                    Log.e("MenuItemViewModel", "Update Menu Item Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<MenuItemResponse>>, t: Throwable) {
                Log.e("MenuItemViewModel", "Update Menu Item Failed: ${t.message}")
            }
        })
    }

    // Xóa món ăn
    fun deleteMenuItem(id: Int) {
        menuItemApi.deleteMenuItem(id).enqueue(object : Callback<ApiResponse<Boolean>> {
            override fun onResponse(call: Call<ApiResponse<Boolean>>, response: Response<ApiResponse<Boolean>>) {
                if (response.isSuccessful) {
                    _deleteMenuItemResult.value = response.body()
                } else {
                    Log.e("MenuItemViewModel", "Delete Menu Item Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                Log.e("MenuItemViewModel", "Delete Menu Item Failed: ${t.message}")
            }
        })
    }
}
