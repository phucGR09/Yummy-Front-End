package com.example.yummy

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yummy.api.AuthenticationApi
import com.example.yummy.ui.theme.YummyTheme
import com.example.yummy.viewmodel.AuthenticationViewModel
import com.example.yummy.models.AuthenticateRequest
import com.example.yummy.viewmodel.AuthenticationViewModelFactory

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YummyTheme {
                SignInScreen(
                    onSignInSuccess = {
                        Toast.makeText(this, "Sign-in successful!", Toast.LENGTH_SHORT).show()
                        finish() // Navigate back or go to the home screen
                    },
                    onSignUpClick = { finish() } // Navigate to the previous screen
                )
            }
        }
    }
}

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val context = LocalContext.current
    val authenticationApi = AuthenticationApi.create()  // Initialize your API client
    val authenticationViewModel = viewModel<AuthenticationViewModel>(
        factory = AuthenticationViewModelFactory(authenticationApi)
    )

    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Lắng nghe kết quả login từ ViewModel
    authenticationViewModel.loginResult.value?.let {
        it.result?.let { loginResponse ->
            // Lưu thông tin người dùng vào SharedPreferences
            val sharedPreferences = context.getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("userToken", loginResponse.token)
            editor.putString("userId", loginResponse.user.id.toString())
            editor.putString("userName", loginResponse.user.userName)
            editor.putString("userEmail", loginResponse.user.email)
            editor.putString("userFullName", loginResponse.user.fullName)
            editor.putString("userPhone", loginResponse.user.phoneNumber)
            editor.putString("userType", loginResponse.user.userType)
            editor.apply()

            // Chuyển đến trang Home sau khi đăng nhập thành công
            onSignInSuccess()
        }
    }

    // Hiển thị thông báo lỗi nếu có
    LaunchedEffect(authenticationViewModel.loginResult.value) {
        if (authenticationViewModel.loginResult.value == null) {
            errorMessage = "Login failed. Please try again."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign In", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            label = { Text("Email/Phone") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (emailOrPhone.isNotEmpty() && password.isNotEmpty()) {
                    val request = AuthenticateRequest(emailOrPhone, password)
                    authenticationViewModel.loginUser(request)
                } else {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onSignUpClick) {
            Text("Don't have an account? Sign up")
        }
    }
}
