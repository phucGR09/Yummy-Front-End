package com.example.yummy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yummy.api.MenuItemApi

class MenuItemViewModelFactory(
    private val menuItemApi: MenuItemApi
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuItemViewModel::class.java)) {
            return MenuItemViewModel(menuItemApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
