package com.example.yummy

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.yummy.api.AuthenticationApi
import com.example.yummy.ui.theme.YummyTheme
import com.example.yummy.viewmodel.AuthenticationViewModel
import com.example.yummy.models.AuthenticateRequest
import com.example.yummy.viewmodel.AuthenticationViewModelFactory

//class SignInActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            YummyTheme {
//                SignInScreen(
//                    onSignInSuccess = {
//                        Toast.makeText(this, "Sign-in successful!", Toast.LENGTH_SHORT).show()
//                        finish() // Navigate back or go to the home screen
//                    },
//                    onSignUpClick = { finish() } // Navigate to the previous screen
//                )
//            }
//        }
//    }
//}

@OptIn(UnstableApi::class)
@Composable
fun SignInScreen(
    menuModel: MenuModel,
    onSignInSuccess: (String) -> Unit,
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
    val loginResult = authenticationViewModel.loginResult.collectAsState().value

    LaunchedEffect(loginResult) {
        loginResult?.let { response ->
            response.result?.let { authenticateResponse ->
                val token = authenticateResponse.token
                val userType = authenticateResponse.userType

                // Lưu thông tin đăng nhập
                ApiClient.setAuthToken(token)
                AuthManager.saveAuthData(context, token, userType)

                if (userType == "RESTAURANT_OWNER") {
                    // Sử dụng giá trị emailOrPhone làm username
                    val username = emailOrPhone

                    // Gọi API để lấy danh sách nhà hàng
                    val restaurants = menuModel.fetchRestaurants(username)
                    if (restaurants != null && restaurants.isNotEmpty()) {
                        val firstRestaurantId = restaurants[0].id

                        // Gán restaurantId mặc định
                        ApiClient.setRestaurantId(firstRestaurantId)
                        SharedData.restaurantId = firstRestaurantId

                        // Gọi hàm tải danh sách món ăn
                        menuModel.getDishesByRestaurantId(firstRestaurantId)
                    } else {
                        errorMessage = "No restaurants found for user."
                        return@LaunchedEffect
                    }
                }

                // Đăng nhập thành công
                Toast.makeText(context, "Sign-in successful!", Toast.LENGTH_SHORT).show()
                onSignInSuccess(userType)

                // Đặt lại trạng thái loginResult để tránh lặp
                authenticationViewModel.resetLoginResult()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Đăng nhập",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall
        )

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
            label = { Text("Mật khẩu") },
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
                    Log.d("SignInScreen", "Email/Phone: $emailOrPhone")
                    Log.d("SignInScreen", "Password: $password")
                    val request = AuthenticateRequest(emailOrPhone, password)
                    authenticationViewModel.loginUser(request)
                } else {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Đăng nhập")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onSignUpClick) {
            Text("Chưa có tài khoản? Đăng ký")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SignInScreenPreview() {
//    val fakeSharedPreferences = object : SharedPreferences {
//        override fun getAll(): MutableMap<String, *> = mutableMapOf("username" to "user_test", "password" to "Password123")
//
//        override fun getString(key: String?, defValue: String?): String? {
//            return when (key) {
//                "username" -> "user_test"
//                "password" -> "Password123"
//                else -> defValue
//            }
//        }
//
//        // Unused methods for this preview
//        override fun contains(key: String?): Boolean = false
//        override fun edit(): SharedPreferences.Editor = throw UnsupportedOperationException()
//        override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? = null
//        override fun getInt(key: String?, defValue: Int): Int = defValue
//        override fun getLong(key: String?, defValue: Long): Long = defValue
//        override fun getFloat(key: String?, defValue: Float): Float = defValue
//        override fun getBoolean(key: String?, defValue: Boolean): Boolean = defValue
//        override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
//        override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
//    }
//
//    SignInScreen(
//        sharedPreferences = fakeSharedPreferences,
//        onSignInSuccess = {},
//        onSignUpClick = {}
//    )
//}
