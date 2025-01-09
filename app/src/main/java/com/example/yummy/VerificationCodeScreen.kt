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

@Composable
fun VerificationCodeScreen(
    name: String,
    contactInfo: String,
    password: String,
    role: String,
    generatedOtp: String,
    onVerifySuccess: () -> Unit,
    onResendOtp: () -> String
) {
    var otpInput by remember { mutableStateOf("") }
    var currentOtp by remember { mutableStateOf(generatedOtp) } // Store the current OTP
    val context = LocalContext.current

    // Send OTP when the screen opens
    LaunchedEffect(Unit) {
        Toast.makeText(
            context,
            "OTP sent to ${if (contactInfo.contains("@")) "email" else "phone"}: $contactInfo. OTP: $currentOtp",
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
        Text("Verify Your Account", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Enter the OTP sent to your ${if (contactInfo.contains("@")) "email" else "phone"}:",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otpInput,
            onValueChange = { if (it.length <= 4) otpInput = it },
            label = { Text("Enter OTP") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (otpInput == currentOtp) { // Verify the OTP
                    onVerifySuccess()
                    Toast.makeText(context, "Verification successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                val newOtp = onResendOtp() // Fetch new OTP
                currentOtp = newOtp // Update the current OTP
                Toast.makeText(context, "New OTP sent!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Resend OTP")
        }
    }
}
