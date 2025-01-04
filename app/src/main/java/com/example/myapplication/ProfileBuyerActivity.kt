package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileBuyerActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_buyer)

        val backIcon = findViewById<ImageView>(R.id.icon_back)
        val nameEditText = findViewById<EditText>(R.id.edit_text_name)
        val emailEditText = findViewById<EditText>(R.id.edit_text_email)
        val phoneEditText = findViewById<EditText>(R.id.edit_text_phone)
        val addressEditText = findViewById<EditText>(R.id.edit_text_address)
        val saveButton = findViewById<Button>(R.id.button_save)

        // Nhận thông tin từ Intent
        val name = intent.getStringExtra("name") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val address = intent.getStringExtra("address") ?: ""

        // Hiển thị thông tin mặc định
        nameEditText.setText(name)
        emailEditText.setText(email)
        phoneEditText.setText(phone)
        addressEditText.setText(address)

        // Vô hiệu hóa chỉnh sửa cả hai trường
        if (email.isNotEmpty()) {
            emailEditText.isEnabled = false // Vô hiệu hóa chỉnh sửa email khi đăng ký bằng email
            phoneEditText.isEnabled = true // Cho phép chỉnh sửa số điện thoại nếu có
        }

        if (phone.isNotEmpty()) {
            phoneEditText.isEnabled = false // Vô hiệu hóa chỉnh sửa số điện thoại khi đăng ký bằng số điện thoại
            emailEditText.isEnabled = true // Cho phép chỉnh sửa email nếu có
        }

        // Quay lại màn hình trước đó
        backIcon.setOnClickListener {
            finish()
        }

        // Nút lưu thông tin
        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString().trim()
            val updatedEmail = emailEditText.text.toString().trim()
            val updatedPhone = phoneEditText.text.toString().trim()
            val updatedAddress = addressEditText.text.toString().trim()

            if (updatedName.isEmpty() || updatedAddress.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty() && updatedPhone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số điện thoại hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lưu thông tin vào SharedPreferences
            sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("name", updatedName)
            editor.putString("address", updatedAddress)

            // Giữ nguyên giá trị email hoặc số điện thoại đã đăng ký
            if (email.isNotEmpty()) {
                editor.putString("email", email)
            }
            if (phone.isNotEmpty()) {
                editor.putString("phone", phone)
            }

            editor.apply()

            Toast.makeText(this, "Thông tin đã được cập nhật thành công!", Toast.LENGTH_SHORT).show()
            finish() // Đóng màn hình sau khi lưu
        }
    }
}
