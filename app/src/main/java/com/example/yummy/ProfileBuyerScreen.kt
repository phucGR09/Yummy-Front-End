package com.example.yummy
import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.yummy.viewmodel.AuthenticationViewModel
import com.example.yummy.models.YeuCauHoanThanhKhachHang

import com.example.yummy.viewmodel.AuthenticationViewModelFactory
import com.example.yummy.api.AuthenticationApi

@Composable
fun ProfileBuyerScreen(
    fullName: String,
    username: String,
    email: String,
    phoneNumber: String,
    address: String,
    onSave: (String, String, String, String) -> Unit,
    viewModel: AuthenticationViewModel = viewModel(
        factory = AuthenticationViewModelFactory(AuthenticationApi.create())
    )
) {
    var updatedFullName by remember { mutableStateOf(fullName) }
    var updatedEmail by remember { mutableStateOf(email) }
    var updatedPhoneNumber by remember { mutableStateOf(phoneNumber) }
    var updatedAddress by remember { mutableStateOf(address) }
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

        Text("Hồ sơ người mua hàng", fontSize = 24.sp, color = Color.Black)

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
            value = updatedAddress,
            onValueChange = { updatedAddress = it },
            label = { Text("Địa chỉ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.completeCustomer(
                    YeuCauHoanThanhKhachHang(
                        address = updatedAddress,
                        username = username
                    )
                ) { response ->
                    if (response.code == 200) {
                        Toast.makeText(context, "Tạo khách hàng thành công!", Toast.LENGTH_SHORT).show()
                        Log.d("CustomerCreation", "Customer creation successful: ${response.result}")
                    } else {
                        Toast.makeText(context, "Lỗi: ${response.message}", Toast.LENGTH_SHORT).show()
                        Log.e("CustomerCreationError", "Error creating customer: Code=${response.code}, Message=${response.message}")
                    }
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
fun ProfileBuyerScreenPreview() {
    ProfileBuyerScreen(
        fullName = "Nguyễn Văn A",
        username = "nguyenvana",
        email = "nguyenvana@example.com",
        phoneNumber = "0123456789",
        address = "123 Đường ABC, Phường XYZ, Thành phố QWE",
        onSave = { fullName, email, phoneNumber, address ->
            // Mock save action for preview
        }
    )
}
