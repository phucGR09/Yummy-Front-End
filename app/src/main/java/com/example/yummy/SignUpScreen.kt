package com.example.yummy

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.yummy.models.AuthenticateRequest
import com.example.yummy.models.RegisterRequest
import com.example.yummy.viewmodel.AuthenticationViewModel
import com.example.yummy.viewmodel.AuthenticationViewModelFactory
import com.example.yummy.api.AuthenticationApi
import java.util.Locale
import com.example.yummy.models.UserType
enum class UserType {
    CUSTOMER,
    RESTAURANT_OWNER,
    DELIVERY_DRIVER
}

fun UserType.toDisplayName(): String {
    return when (this) {
        UserType.CUSTOMER -> "Người Mua hàng"
        UserType.RESTAURANT_OWNER -> "Người Bán Hàng"
        UserType.DELIVERY_DRIVER -> "Người Giao Hàng"
        else -> throw IllegalArgumentException("Invalid user type")
    }
}

@OptIn(UnstableApi::class)


@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onSignInClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserType?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authenticationApi = AuthenticationApi.create()
    val authenticationViewModel = viewModel<AuthenticationViewModel>(
        factory = AuthenticationViewModelFactory(authenticationApi)
    )

    // Lắng nghe trạng thái kết quả đăng ký
    val registerResult by authenticationViewModel.registerResult.collectAsState()
    registerResult?.let { response ->
        Log.d("SignUpScreen", "Register Response: $response")
        if (response.code == 200 && response.result != null) {
            Toast.makeText(context, "Register successful!", Toast.LENGTH_SHORT).show()
        } else {
            errorMessage = response.message ?: "Register failed. Try again."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Đăng Ký", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Tên Đăng Nhập") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật Khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Xác Nhận Mật Khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Role Selection with Radio Buttons
        Text("Chọn Vai Trò", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        RoleSelectionRadioButton(
            selectedRole = selectedRole,
            onRoleSelected = { role -> selectedRole = role }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                when {
                    username.isEmpty() || email.isEmpty() || fullName.isEmpty() || phone.isEmpty() ||
                            password.isEmpty() || confirmPassword.isEmpty() -> {
                        Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                    }
                    selectedRole == null -> {
                        Toast.makeText(context, "Please select a role", Toast.LENGTH_SHORT).show()
                    }
                    password != confirmPassword -> {
                        Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                    }
                    !isValidPassword(password) -> {
                        Toast.makeText(
                            context,
                            "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    !isValidEmail(email) -> {
                        Toast.makeText(context, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                    }
                    !isValidPhoneNumber(phone) -> {
                        Toast.makeText(context, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        isLoading = true
                        val request = RegisterRequest(
                            username = username,
                            password = password,
                            email = email,
                            fullName = fullName,
                            phone = phone,
                            userType = selectedRole?.name ?: ""
                        )
                        // Log thông tin request
                        Log.d("SignUpScreen", "Register Request: $request")
                        authenticationViewModel.registerUser(request)

                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
            } else {
                Text("Sign Up")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onSignInClick) {
            Text("Đã có tài khoản? Đăng nhập")
        }
    }
}

@Composable
fun RoleSelectionRadioButton(
    selectedRole: UserType?, // Kiểu UserType, có thể null
    onRoleSelected: (UserType) -> Unit // Hàm callback khi chọn role
) {
    val roles = listOf(UserType.CUSTOMER, UserType.RESTAURANT_OWNER, UserType.DELIVERY_DRIVER)

    Column {
        roles.forEach { role ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = role == selectedRole,
                    onClick = { onRoleSelected(role) } // Callback với role được chọn
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(role.toDisplayName())
            }
        }
    }
}

fun isValidPhoneNumber(phone: String): Boolean {
    return phone.matches(Regex("^[0-9]{10,11}$"))
}

fun isValidPassword(password: String): Boolean {
    val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$"
    return password.matches(passwordPattern.toRegex())
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    return emailPattern.matcher(email).matches()
}

fun isValidPhone(phone: String): Boolean {
    return phone.matches(Regex("^[0-9]{10,11}$"))
}

//@Preview(showBackground = true)
//@Composable
//fun SignUpScreenPreview() {
//    SignUpScreen(
//        onSignUpSuccess = { _, _, _, _, _, _ -> },
//        onSignInClick = {}
//    )
//}