package com.example.yummy

import android.widget.Toast
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

enum class UserType {
    CUSTOMER, RESTAURANT_OWNER, DELIVERY_DRIVER
}

// Extension function to map UserType to Vietnamese display names
fun UserType.toDisplayName(): String {
    return when (this) {
        UserType.CUSTOMER -> "Người Mua hàng"
        UserType.RESTAURANT_OWNER -> "Người Bán Hàng"
        UserType.DELIVERY_DRIVER -> "Người Giao Hàng"
    }
}

@Composable
fun SignUpScreen(
    onSignUpSuccess: (String, String, String, String, String, UserType) -> Unit,
    onSignInClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserType?>(null) }

    val context = LocalContext.current

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
            label = { Text("Họ và Tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Số Điện Thoại") },
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
            onRoleSelected = { selectedRole = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    fullName.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                        Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }
                    selectedRole == null -> {
                        Toast.makeText(context, "Vui lòng chọn vai trò", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, "Vui lòng nhập địa chỉ email hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                    !isValidPhone(phone) -> {
                        Toast.makeText(context, "Vui lòng nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Perform sign-up logic
                        onSignUpSuccess(fullName, username, email, phone, password, selectedRole!!)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Đăng Ký")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onSignInClick) {
            Text("Đã có tài khoản? Đăng nhập")
        }
    }
}

@Composable
fun RoleSelectionRadioButton(
    selectedRole: UserType?,
    onRoleSelected: (UserType) -> Unit
) {
    val roles = UserType.values()

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
                    onClick = { onRoleSelected(role) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(role.toDisplayName())
            }
        }
    }
}

fun isValidPassword(password: String): Boolean {
    val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\\\$%^&+=]).{8,}$"
    return password.matches(passwordPattern.toRegex())
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    return emailPattern.matcher(email).matches()
}

fun isValidPhone(phone: String): Boolean {
    return phone.matches(Regex("^[0-9]{10,11}$"))
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        onSignUpSuccess = { _, _, _, _, _, _ -> },
        onSignInClick = {}
    )
}