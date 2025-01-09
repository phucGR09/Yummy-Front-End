package com.example.yummy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yummy.api.AuthenticationApi

class AuthenticationViewModelFactory(
    private val authenticationApi: AuthenticationApi
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            return AuthenticationViewModel(authenticationApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
