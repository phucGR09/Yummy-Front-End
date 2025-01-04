package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)

        val emailInput = findViewById<EditText>(R.id.email_input)
        val sendOtpButton = findViewById<Button>(R.id.send_otp_button)

        sendOtpButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email hoặc số điện thoại.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Giả lập kiểm tra email tồn tại
            if (isEmailRegistered(email)) {
                // Chuyển sang VerificationCodeForResetActivity
                val intent = Intent(this, VerificationCodeActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("isForResetPassword", true)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Email/SĐT không tồn tại.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isEmailRegistered(email: String): Boolean {
        // Logic giả lập: kiểm tra email trong SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val registeredEmail = sharedPreferences.getString("email", null)
        return email == registeredEmail
    }
}
