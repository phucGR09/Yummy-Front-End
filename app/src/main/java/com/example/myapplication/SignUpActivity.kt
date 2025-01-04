package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        val nameEditText = findViewById<EditText>(R.id.edit_text_name)
        val contactEditText = findViewById<EditText>(R.id.edit_text_email) // Một ô cho email hoặc số điện thoại
        val passwordEditText = findViewById<EditText>(R.id.edit_text_password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.edit_text_confirm_password)
        val radioGroupRole = findViewById<RadioGroup>(R.id.radio_group_role)
        val signUpButton = findViewById<Button>(R.id.button_sign_up)
        val signInLink = findViewById<TextView>(R.id.textView3)

        // SharedPreferences để lưu trữ thông tin người dùng
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val contactInfo = contactEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val selectedRoleId = radioGroupRole.checkedRadioButtonId


            val selectedRole = findViewById<RadioButton>(selectedRoleId).text.toString()

            if (name.isEmpty() || contactInfo.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedRoleId == -1) {
                Toast.makeText(this, "Vui lòng chọn vai trò.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu không khớp.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                Toast.makeText(
                    this,
                    "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, chữ số và ký tự đặc biệt.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (!isValidContactInfo(contactInfo)) {
                Toast.makeText(this, "Vui lòng nhập email hoặc số điện thoại hợp lệ.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kiểm tra xem email hoặc số điện thoại đã được đăng ký chưa
            if (isContactInfoRegistered(sharedPreferences, contactInfo)) {
                Toast.makeText(this, "Email hoặc số điện thoại đã được đăng ký.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Chuyển sang màn hình xác minh OTP
            val intent = Intent(this, VerificationCodeActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("contactInfo", contactInfo)
            intent.putExtra("password", password)
            intent.putExtra("role", selectedRole)
            startActivity(intent)
        }

        signInLink.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=]).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }

    private fun isValidContactInfo(contactInfo: String): Boolean {
        return if (Patterns.EMAIL_ADDRESS.matcher(contactInfo).matches()) {
            true // Là email hợp lệ
        } else {
            contactInfo.matches(Regex("^[0-9]{10,11}$")) // Là số điện thoại hợp lệ (10-11 số)
        }
    }

    private fun isContactInfoRegistered(sharedPreferences: SharedPreferences, contactInfo: String): Boolean {
        val registeredEmails = sharedPreferences.getStringSet("registeredEmails", setOf()) ?: setOf()
        val registeredPhones = sharedPreferences.getStringSet("registeredPhones", setOf()) ?: setOf()

        return contactInfo in registeredEmails || contactInfo in registeredPhones
    }
}
