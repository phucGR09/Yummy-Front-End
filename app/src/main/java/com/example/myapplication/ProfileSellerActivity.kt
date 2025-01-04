package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileSellerActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val REQUEST_CODE_CERTIFICATE = 1001
    private val REQUEST_CODE_CCCD = 1002
    private var certificateImageUri: Uri? = null
    private var cccdImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_seller)

        val backIcon = findViewById<ImageView>(R.id.icon_back)
        val nameEditText = findViewById<EditText>(R.id.edit_text_name)
        val emailEditText = findViewById<EditText>(R.id.edit_text_email)
        val phoneEditText = findViewById<EditText>(R.id.edit_text_phone)
        val timeEditText = findViewById<EditText>(R.id.edit_text_time)
        val addressEditText = findViewById<EditText>(R.id.edit_text_address)
        val saveButton = findViewById<Button>(R.id.button_save)
        val uploadCertificateButton = findViewById<Button>(R.id.button_upload_certificate)
        val uploadCccdButton = findViewById<Button>(R.id.button_upload_cccd)

        // Nhận thông tin từ Intent
        val name = intent.getStringExtra("name") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val time = intent.getStringExtra("time") ?: ""
        val address = intent.getStringExtra("address") ?: ""

        // Hiển thị thông tin mặc định
        nameEditText.setText(name)
        emailEditText.setText(email)
        phoneEditText.setText(phone)
        timeEditText.setText(time)
        addressEditText.setText(address)

        // Vô hiệu hóa chỉnh sửa email hoặc số điện thoại
        emailEditText.isEnabled = email.isEmpty()
        phoneEditText.isEnabled = phone.isEmpty()

        // Quay lại màn hình trước đó
        backIcon.setOnClickListener {
            finish()
        }

        // Nút tải ảnh chứng nhận
        uploadCertificateButton.setOnClickListener {
            openGallery(REQUEST_CODE_CERTIFICATE)
        }

        // Nút tải ảnh CCCD
        uploadCccdButton.setOnClickListener {
            openGallery(REQUEST_CODE_CCCD)
        }

        // Nút lưu thông tin
        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString().trim()
            val updatedTime = timeEditText.text.toString().trim()
            val updatedAddress = addressEditText.text.toString().trim()

            if (updatedName.isEmpty() || updatedAddress.isEmpty() || updatedTime.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lưu thông tin vào SharedPreferences
            sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("name", updatedName)
            editor.putString("time", updatedTime)
            editor.putString("address", updatedAddress)

            // Giữ nguyên email hoặc số điện thoại đã đăng ký
            if (email.isNotEmpty()) {
                editor.putString("email", email)
            }
            if (phone.isNotEmpty()) {
                editor.putString("phone", phone)
            }

            // Lưu đường dẫn ảnh vào SharedPreferences (nếu có)
            certificateImageUri?.let {
                editor.putString("certificateUri", it.toString())
            }
            cccdImageUri?.let {
                editor.putString("cccdUri", it.toString())
            }

            editor.apply()

            Toast.makeText(this, "Thông tin đã được cập nhật thành công!", Toast.LENGTH_SHORT).show()
            finish() // Đóng màn hình sau khi lưu
        }
    }

    // Hàm mở thư viện ảnh
    private fun openGallery(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            when (requestCode) {
                REQUEST_CODE_CERTIFICATE -> {
                    certificateImageUri = imageUri
                    Toast.makeText(this, "Tải ảnh chứng nhận thành công!", Toast.LENGTH_SHORT).show()
                }
                REQUEST_CODE_CCCD -> {
                    cccdImageUri = imageUri
                    Toast.makeText(this, "Tải ảnh CCCD thành công!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
