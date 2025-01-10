package com.example.yummy

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileShipperScreen(
    fullName: String,
    username: String,
    email: String,
    phoneNumber: String,
    cccd: String,
    license: String,
    avatarUrl: String,
    onSave: (String, String, String, String, String, String, String) -> Unit
) {
    var updatedFullName by remember { mutableStateOf(fullName) }
    var updatedEmail by remember { mutableStateOf(email) }
    var updatedPhoneNumber by remember { mutableStateOf(phoneNumber) }
    var updatedCCCD by remember { mutableStateOf(cccd) }
    var updatedLicense by remember { mutableStateOf(license) }
    var updatedAvatarUrl by remember { mutableStateOf(avatarUrl) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Avatar Image at the top
        Image(
            painter = painterResource(id = R.drawable.face), // Replace with your avatar resource
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        Text("Hồ sơ người giao hàng", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedFullName,
            onValueChange = { updatedFullName = it },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {},
            label = { Text("Tên đăng nhập") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Disable editing for username
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedEmail,
            onValueChange = { updatedEmail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedPhoneNumber,
            onValueChange = { updatedPhoneNumber = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedCCCD,
            onValueChange = { updatedCCCD = it },
            label = { Text("CCCD") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedLicense,
            onValueChange = { updatedLicense = it },
            label = { Text("Giấy phép lái xe") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedAvatarUrl,
            onValueChange = { updatedAvatarUrl = it },
            label = { Text("Avatar URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (updatedFullName.isEmpty() || updatedEmail.isEmpty() || updatedPhoneNumber.isEmpty() || updatedCCCD.isEmpty() || updatedLicense.isEmpty() || updatedAvatarUrl.isEmpty()) {
                    Toast.makeText(context, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                } else {
                    onSave(
                        updatedFullName,
                        username, // Add username parameter
                        updatedEmail,
                        updatedPhoneNumber,
                        updatedCCCD,
                        updatedLicense,
                        updatedAvatarUrl
                    )
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LƯU")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ProfileShipperScreenPreview() {
    ProfileShipperScreen(
        fullName = "Nguyễn Văn A",
        username = "nguyenvana",
        email = "nguyenvana@example.com",
        phoneNumber = "0123456789",
        cccd = "123456789",
        license = "987654321",
        avatarUrl = "https://example.com/avatar.jpg",
        onSave = { fullName, username, email, phoneNumber, cccd, license, avatarUrl ->
            // Mock save action for preview
        }
    )
}
