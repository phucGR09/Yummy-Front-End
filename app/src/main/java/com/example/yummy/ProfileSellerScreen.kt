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
import com.example.yummy.viewmodel.AuthenticationViewModelFactory
import com.example.yummy.api.AuthenticationApi
import com.example.yummy.viewmodel.AuthenticationViewModel
import com.example.yummy.models.GioMoCua
import com.example.yummy.models.YeuCauTaoNhaHang
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileSellerScreen(
    username: String,
    fullName: String,
    shopName: String, // Thêm trường Tên cửa hàng
    address: String,
    openingHours: String,
    taxCode: String,
    email: String,
    phoneNumber: String,
    onSave: (String, String, String, String, String, String, String) -> Unit // Thêm shopName vào callback
) {
    val viewModel: AuthenticationViewModel = viewModel(
        factory = AuthenticationViewModelFactory(AuthenticationApi.create())
    )

    var updatedFullName by remember { mutableStateOf(fullName) }
    var updatedShopName by remember { mutableStateOf(shopName) } // Biến trạng thái cho Tên cửa hàng
    var updatedAddress by remember { mutableStateOf(address) }
    var updatedOpeningHours by remember { mutableStateOf(openingHours) }
    var updatedTaxCode by remember { mutableStateOf(taxCode) }
    var updatedEmail by remember { mutableStateOf(email) }
    var updatedPhoneNumber by remember { mutableStateOf(phoneNumber) }
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

        Text("Hồ sơ người bán hàng", fontSize = 24.sp, color = Color.Black)

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
            value = updatedFullName,
            onValueChange = { updatedFullName = it },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedShopName,
            onValueChange = { updatedShopName = it },
            label = { Text("Tên cửa hàng") },
            modifier = Modifier.fillMaxWidth()
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

        OutlinedTextField(
            value = updatedOpeningHours,
            onValueChange = { updatedOpeningHours = it },
            label = { Text("Giờ mở cửa") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedTaxCode,
            onValueChange = { updatedTaxCode = it },
            label = { Text("Mã số thuế") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (updatedShopName.isBlank() || updatedAddress.isBlank() || updatedOpeningHours.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val openingHoursParts = updatedOpeningHours.split(":")
                val openingHours = GioMoCua(
                    hour = openingHoursParts[0].toInt(),
                    minute = openingHoursParts[1].toInt(),
                    second = 0,
                    nano = 0
                )
                val request = YeuCauTaoNhaHang(
                    name = updatedShopName,
                    address = updatedAddress,
                    openingHours = openingHours,
                    username = username
                )

                viewModel.taoNhaHang(request) { response ->
                    if (response.code == 200) {
                        Toast.makeText(context, "Tạo cửa hàng thành công!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Lỗi: ${response.message}", Toast.LENGTH_SHORT).show()
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
fun ProfileSellerScreenPreview() {
    ProfileSellerScreen(
        username = "nguyenvana",
        fullName = "Nguyễn Văn A",
        shopName = "Cửa Hàng XYZ", // Thêm tên cửa hàng mẫu
        address = "123 Đường ABC, Phường XYZ, Thành phố QWE",
        openingHours = "8:00 - 17:00",
        taxCode = "123456789",
        email = "nguyenvana@example.com",
        phoneNumber = "0123456789",
        onSave = { fullName, shopName, address, openingHours, taxCode, email, phoneNumber ->
            // Mock save action for preview
        }
    )
}
