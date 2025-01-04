package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileShipperActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val REQUEST_CODE_CCCD = 1
    private val REQUEST_CODE_LICENSE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_shipper)

        val backIcon = findViewById<ImageView>(R.id.icon_back)
        val nameEditText = findViewById<EditText>(R.id.edit_text_name)
        val emailEditText = findViewById<EditText>(R.id.edit_text_email)
        val phoneEditText = findViewById<EditText>(R.id.edit_text_phone)
        val addressEditText = findViewById<EditText>(R.id.edit_text_address)
        val uploadCccdButton = findViewById<Button>(R.id.button_upload_cccd)
        val uploadLicenseButton = findViewById<Button>(R.id.button_upload_license)
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

        // Vô hiệu hóa chỉnh sửa email hoặc số điện thoại
        if (email.isNotEmpty()) {
            emailEditText.isEnabled = false // Email không thể chỉnh sửa
            phoneEditText.isEnabled = true // Cho phép chỉnh sửa số điện thoại
        } else if (phone.isNotEmpty()) {
            phoneEditText.isEnabled = false // Số điện thoại không thể chỉnh sửa
            emailEditText.isEnabled = true // Cho phép chỉnh sửa email
        }

        // Quay lại màn hình trước đó
        backIcon.setOnClickListener {
            finish()
        }

        // Nút tải ảnh CCCD
        uploadCccdButton.setOnClickListener {
            openGallery(REQUEST_CODE_CCCD)
        }

        // Nút tải ảnh giấy phép lái xe
        uploadLicenseButton.setOnClickListener {
            openGallery(REQUEST_CODE_LICENSE)
        }

        // Nút lưu thông tin
        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString().trim()
            val updatedAddress = addressEditText.text.toString().trim()

            if (updatedName.isEmpty() || updatedAddress.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lưu thông tin vào SharedPreferences
            sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("name", updatedName)
            editor.putString("address", updatedAddress)

            // Giữ nguyên email hoặc số điện thoại đã đăng ký
            if (email.isNotEmpty()) {
                editor.putString("email", email) // Không thay đổi email
            }
            if (phone.isNotEmpty()) {
                editor.putString("phone", phone) // Không thay đổi số điện thoại
            }

            editor.apply()

            Toast.makeText(this, "Thông tin đã được cập nhật thành công!", Toast.LENGTH_SHORT).show()
            finish() // Đóng màn hình sau khi lưu
        }
    }

    private fun openGallery(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            when (requestCode) {
                REQUEST_CODE_CCCD -> {
                    Toast.makeText(this, "Tải ảnh CCCD thành công!", Toast.LENGTH_SHORT).show()
                }
                REQUEST_CODE_LICENSE -> {
                    Toast.makeText(this, "Tải ảnh giấy phép lái xe thành công!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
