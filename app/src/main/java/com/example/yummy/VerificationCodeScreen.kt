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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview

data class SignUpData(
    val fullName: String,
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: UserType
)

@Composable
fun VerificationCodeScreen(
    signUpData: SignUpData,
    generatedOtp: String,
    onVerifySuccess: (SignUpData) -> Unit,
    onResendOtp: () -> String
) {
    var otpInput by remember { mutableStateOf("") }
    var currentOtp by remember { mutableStateOf(generatedOtp) } // Store the current OTP
    val context = LocalContext.current

    // Send OTP when the screen opens
    LaunchedEffect(Unit) {
        Toast.makeText(
            context,
            "Mã OTP đã được gửi đến số điện thoại: ${signUpData.phone}. Mã OTP: $currentOtp",
            Toast.LENGTH_SHORT
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Xác minh tài khoản", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Nhập mã OTP được gửi đến số điện thoại:",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otpInput,
            onValueChange = { if (it.length <= 4) otpInput = it },
            label = { Text("Nhập mã OTP") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (otpInput == currentOtp) { // Verify the OTP
                    onVerifySuccess(signUpData)
                    Toast.makeText(context, "Xác minh thành công!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Mã OTP không đúng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác minh")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                val newOtp = onResendOtp() // Fetch new OTP
                currentOtp = newOtp // Update the current OTP
                Toast.makeText(context, "Mã OTP mới đã được gửi!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Gửi lại mã OTP")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationCodeScreenPreview() {
    VerificationCodeScreen(
        signUpData = SignUpData(
            fullName = "Nguyễn Văn A",
            username = "nguyenvana",
            email = "nguyenvana@example.com",
            phone = "0123456789",
            password = "password123",
            role = UserType.CUSTOMER
        ),
        generatedOtp = "1234",
        onVerifySuccess = {
            // Handle verification success
        },
        onResendOtp = {
            // Simulate OTP resend
            "5678"
        }
    )
}
