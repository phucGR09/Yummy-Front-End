package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        val contactEditText = findViewById<EditText>(R.id.edit_text_email) // Trường chung cho email/số điện thoại
        val passwordEditText = findViewById<EditText>(R.id.edit_text_password)
        val loginButton = findViewById<Button>(R.id.button_login)
        val signUpLink = findViewById<TextView>(R.id.textView4)
        val forgotPassword = findViewById<TextView>(R.id.forgot_password)

        // Xử lý khi nhấn nút "Đăng nhập"
        loginButton.setOnClickListener {
            val contactInfo = contactEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (contactInfo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (validateLogin(contactInfo, password)) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                navigateToWelcomeScreen()
            } else {
                Toast.makeText(this, "Email/Số điện thoại hoặc mật khẩu không chính xác.", Toast.LENGTH_SHORT).show()
            }
        }

        // Chuyển đến màn hình "Đăng ký"
        signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Xử lý "Quên mật khẩu"
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateLogin(contactInfo: String, password: String): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val registeredEmail = sharedPreferences.getString("email", null)
        val registeredPhone = sharedPreferences.getString("phone", null) // Số điện thoại nếu có
        val registeredPassword = sharedPreferences.getString("password", null)

        return ((contactInfo == registeredEmail || contactInfo == registeredPhone) && password == registeredPassword)
    }

    private fun navigateToWelcomeScreen() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
