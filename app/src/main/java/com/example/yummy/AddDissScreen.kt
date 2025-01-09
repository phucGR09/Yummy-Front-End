package com.example.yummy

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.Normalizer
import java.util.regex.Pattern
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Preview(showBackground = true)
@Composable
fun PreviewAddDishScreen() {
    val navController = rememberNavController()
    val viewModel = MenuSellerViewModel()

    // Hiển thị màn hình thêm món ăn
    AddDishScreen(navController = navController, viewModel = viewModel)
}

@Composable
fun AddDishScreen(
    navController: NavController,
    viewModel: MenuSellerViewModel
) {
    val context = LocalContext.current

    // State quản lý thông tin món ăn mới
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") } // Đường dẫn ảnh hiện tại
    var isUploading by remember { mutableStateOf(false) } // Trạng thái upload ảnh

    // Bộ khởi chạy để mở thư viện ảnh
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            isUploading = true // Bắt đầu upload ảnh
            viewModel.updateImage(Dish(0,1,name, price.toIntOrNull() ?: 0, description, null), selectedUri.toString()) { success ->
                isUploading = false // Kết thúc upload
                if (success) {
                    imagePath = selectedUri.toString() // Cập nhật ảnh hiển thị
                } else {
                    Toast.makeText(context, "Upload ảnh thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, // Sử dụng icon mặc định của Material Icons
                contentDescription = "Back",
                tint = Color(0xFFFF5722), // Đặt màu của icon
                modifier = Modifier
                    .size(23.dp) // Kích thước icon
                    .clickable { navController.popBackStack() } // Thêm hành động khi click
            )

            Text(
                text = "Thêm món ăn",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Form thêm món ăn
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hiển thị ảnh món ăn
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (imagePath.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imagePath)
                            .crossfade(true)
                            .size(160) // Giới hạn kích thước tải về
                            .build(),
                        contentDescription = "Ảnh món ăn",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.upload_your_photo), // Hình mặc định
                        contentDescription = "Ảnh mặc định",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(6.dp)) // Tạo khoảng cách giữa ảnh và nút

                // Nút thay đổi ảnh
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                ) {
                    Text(if (isUploading) "Đang tải..." else "Thay đổi ảnh", color = Color.White)
                }
            }

            // Tên món ăn
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên món ăn") },
                modifier = Modifier.fillMaxWidth()
            )

            // Giá tiền
            TextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Giá tiền (VNĐ)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Mô tả món ăn
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Mô tả món ăn (tùy chọn)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Nút Lưu và Hủy
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nút Hủy
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("HỦY", color = Color.Black)
            }

            // Nút Lưu
            Button(
                onClick = {
                    if (name.isNotEmpty() && price.isNotEmpty()) {
                        val newDish = Dish(
                            item_id = 0,
                            restaurant_id =1,
                            name = name,
                            price = price.toIntOrNull() ?: 0,
                            description = description,
                            imagePath = imagePath
                        )
                        viewModel.addDish(newDish)
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                modifier = Modifier.weight(1f)
            ) {
                Text("THÊM MÓN ĂN", color = Color.White)
            }
        }
    }
}
