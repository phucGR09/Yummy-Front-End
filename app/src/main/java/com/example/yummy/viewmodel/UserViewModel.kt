package com.example.yummy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yummy.models.User
import com.example.yummy.models.UserCreationRequest
import com.example.yummy.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    // Lấy tất cả người dùng
    fun getAllUsers() {
        userRepository.getAllUsers { users ->
            _users.postValue(users)
        }
    }

    // Tạo người dùng mới
    fun createUser(userCreationRequest: UserCreationRequest) {
        userRepository.createUser(userCreationRequest) { user ->
            _user.postValue(user)
        }
    }
}
