package com.example.yummy

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
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

@Composable
fun SignInScreen(
    sharedPreferences: SharedPreferences, // Add SharedPreferences as a parameter
    onSignInSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

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
            value = username,
            onValueChange = { username = it },
            label = { Text("Tên đăng nhập") },
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

        Button(
            onClick = {
                handleSignIn(
                    context = context,
                    sharedPreferences = sharedPreferences,
                    username = username,
                    password = password,
                    onSignInSuccess = onSignInSuccess
                )
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

fun handleSignIn(
    context: Context,
    sharedPreferences: SharedPreferences,
    username: String,
    password: String,
    onSignInSuccess: () -> Unit
) {
    if (username.isBlank() || password.isBlank()) {
        Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        return
    }

    val storedUsername = sharedPreferences.getString("username", null)
    val storedPassword = sharedPreferences.getString("password", null)

    if (username == storedUsername && password == storedPassword) {
        Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
        onSignInSuccess()
    } else {
        Toast.makeText(context, "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    val fakeSharedPreferences = object : SharedPreferences {
        override fun getAll(): MutableMap<String, *> = mutableMapOf("username" to "user_test", "password" to "Password123")

        override fun getString(key: String?, defValue: String?): String? {
            return when (key) {
                "username" -> "user_test"
                "password" -> "Password123"
                else -> defValue
            }
        }

        // Unused methods for this preview
        override fun contains(key: String?): Boolean = false
        override fun edit(): SharedPreferences.Editor = throw UnsupportedOperationException()
        override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? = null
        override fun getInt(key: String?, defValue: Int): Int = defValue
        override fun getLong(key: String?, defValue: Long): Long = defValue
        override fun getFloat(key: String?, defValue: Float): Float = defValue
        override fun getBoolean(key: String?, defValue: Boolean): Boolean = defValue
        override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
        override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
    }

    SignInScreen(
        sharedPreferences = fakeSharedPreferences,
        onSignInSuccess = {},
        onSignUpClick = {}
    )
}
