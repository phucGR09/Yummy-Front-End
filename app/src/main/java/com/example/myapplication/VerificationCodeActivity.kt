package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class VerificationCodeActivity : AppCompatActivity() {

    private var generatedOtp: String = ""
    private var otpEntered = StringBuilder()
    private var isForResetPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification_code)

        // Nhận thông tin từ Intent
        isForResetPassword = intent.getBooleanExtra("isForResetPassword", false)
        val contactInfo = intent.getStringExtra("contactInfo") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        val role = intent.getStringExtra("role") ?: ""

        // Gửi mã OTP
        sendOtpToContactInfo(contactInfo)

        val otp1 = findViewById<EditText>(R.id.otp_1)
        val otp2 = findViewById<EditText>(R.id.otp_2)
        val otp3 = findViewById<EditText>(R.id.otp_3)
        val otp4 = findViewById<EditText>(R.id.otp_4)
        val verifyButton = findViewById<Button>(R.id.button)
        val resendTextView = findViewById<TextView>(R.id.resend_code)

        val numberPad = mapOf(
            1 to findViewById<ImageView>(R.id.num_1),
            2 to findViewById<ImageView>(R.id.num_2),
            3 to findViewById<ImageView>(R.id.num_3),
            4 to findViewById<ImageView>(R.id.num_4),
            5 to findViewById<ImageView>(R.id.num_5),
            6 to findViewById<ImageView>(R.id.num_6),
            7 to findViewById<ImageView>(R.id.num_7),
            8 to findViewById<ImageView>(R.id.num_8),
            9 to findViewById<ImageView>(R.id.num_9),
            0 to findViewById<ImageView>(R.id.num_0)
        )
        val deleteButton = findViewById<ImageView>(R.id.delete)

        // Xử lý nhấn phím số
        numberPad.forEach { (number, imageView) ->
            imageView.setOnClickListener {
                if (otpEntered.length < 4) {
                    otpEntered.append(number)
                    updateOtpFields(otp1, otp2, otp3, otp4)
                }
            }
        }

        // Xử lý nút xóa
        deleteButton.setOnClickListener {
            if (otpEntered.isNotEmpty()) {
                otpEntered.deleteCharAt(otpEntered.length - 1)
                updateOtpFields(otp1, otp2, otp3, otp4)
            }
        }

        // Xử lý nút xác minh
        verifyButton.setOnClickListener {
            if (otpEntered.toString() == generatedOtp) {
                Toast.makeText(this, "Xác thực mã OTP thành công!", Toast.LENGTH_SHORT).show()

                // Lưu thông tin người dùng vào SharedPreferences sau khi xác thực thành công
                saveUserToSharedPreferences(contactInfo, password, name)

                // Chuyển hướng theo vai trò sau khi lưu thành công
                when (role) {
                    "Người mua hàng" -> {
                        val intent = Intent(this, ProfileBuyerActivity::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("contactInfo", contactInfo)
                        intent.putExtra("phone", if (isPhoneNumber(contactInfo)) contactInfo else "")
                        intent.putExtra("email", if (isEmail(contactInfo)) contactInfo else "")
                        intent.putExtra("address", "")
                        startActivity(intent)
                    }
                    "Người bán hàng" -> {
                        val intent = Intent(this, ProfileSellerActivity::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("contactInfo", contactInfo)
                        intent.putExtra("phone", if (isPhoneNumber(contactInfo)) contactInfo else "")
                        intent.putExtra("email", if (isEmail(contactInfo)) contactInfo else "")
                        intent.putExtra("address", "")
                        startActivity(intent)
                    }
                    "Người giao hàng" -> {
                        val intent = Intent(this, ProfileShipperActivity::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("contactInfo", contactInfo)
                        intent.putExtra("phone", if (isPhoneNumber(contactInfo)) contactInfo else "")
                        intent.putExtra("email", if (isEmail(contactInfo)) contactInfo else "")
                        intent.putExtra("address", "")
                        startActivity(intent)
                    }
                    else -> {
                        Toast.makeText(this, "Vai trò không hợp lệ!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                finish() // Đóng màn hình sau khi hoàn tất đăng ký
            } else {
                Toast.makeText(this, "Mã OTP không chính xác. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
            }
        }


        // Xử lý khi người dùng nhấn "Gửi lại"
        resendTextView.setOnClickListener {
            sendOtpToContactInfo(contactInfo)
            Toast.makeText(this, "Mã xác minh đã được gửi lại.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateOtpFields(otp1: EditText, otp2: EditText, otp3: EditText, otp4: EditText) {
        val otpChars = otpEntered.toString().padEnd(4, ' ')
        otp1.setText(otpChars.getOrNull(0)?.toString() ?: "")
        otp2.setText(otpChars.getOrNull(1)?.toString() ?: "")
        otp3.setText(otpChars.getOrNull(2)?.toString() ?: "")
        otp4.setText(otpChars.getOrNull(3)?.toString() ?: "")
    }

    private fun sendOtpToContactInfo(contactInfo: String) {
        generatedOtp = generateOtp()

        if (isEmail(contactInfo)) {
            Toast.makeText(this, "Mã OTP đã được gửi đến email: $contactInfo", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Mã OTP: $generatedOtp", Toast.LENGTH_LONG).show()
        } else if (isPhoneNumber(contactInfo)) {
            Toast.makeText(this, "Mã OTP đã được gửi đến số điện thoại: $contactInfo", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Mã OTP: $generatedOtp", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Liên hệ không hợp lệ!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateOtp(): String {
        return (1000..9999).random().toString()
    }
    private fun saveUserToSharedPreferences(contactInfo: String, password: String, name: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val registeredEmails = sharedPreferences.getStringSet("registeredEmails", mutableSetOf()) ?: mutableSetOf()
        val registeredPhones = sharedPreferences.getStringSet("registeredPhones", mutableSetOf()) ?: mutableSetOf()

        if (isEmail(contactInfo)) {
            registeredEmails.add(contactInfo)
            editor.putStringSet("registeredEmails", registeredEmails)
        } else if (isPhoneNumber(contactInfo)) {
            registeredPhones.add(contactInfo)
            editor.putStringSet("registeredPhones", registeredPhones)
        }

        editor.putString("name", name)
        editor.putString("password", password)
        editor.apply()
    }

    private fun isEmail(contactInfo: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(contactInfo).matches()
    }

    private fun isPhoneNumber(contactInfo: String): Boolean {
        return contactInfo.matches(Regex("^[0-9]{10,11}$"))
    }
}
