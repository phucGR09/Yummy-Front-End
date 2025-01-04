package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_password)

        val newPasswordInput = findViewById<EditText>(R.id.new_password_input)
        val confirmNewPasswordInput = findViewById<EditText>(R.id.confirm_new_password_input)
        val saveButton = findViewById<Button>(R.id.save_button)

        // Lấy email từ Intent
        val email = intent.getStringExtra("email") ?: ""

        saveButton.setOnClickListener {
            val newPassword = newPasswordInput.text.toString().trim()
            val confirmNewPassword = confirmNewPasswordInput.text.toString().trim()

            if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmNewPassword) {
                Toast.makeText(this, "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(newPassword)) {
                Toast.makeText(
                    this,
                    "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, chữ số và ký tự đặc biệt.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            updatePassword(email, newPassword)
            Toast.makeText(this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show()

            // Quay lại màn hình đăng nhập
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=]).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }

    private fun updatePassword(email: String, newPassword: String) {
        // Lưu mật khẩu mới vào SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("password", newPassword) // Cập nhật mật khẩu mới
        editor.apply()
    }
}
