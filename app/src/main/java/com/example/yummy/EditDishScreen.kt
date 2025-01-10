package com.example.yummy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Preview(showBackground = true)
@Composable
fun PreviewEditDishScreen() {
    val navController = rememberNavController()
    val orderModel= MenuModel()
    val viewModel = MenuSellerViewModel(orderModel)

    // Món ăn giả lập để chỉnh sửa
    val dish = Dish(
        itemId = 1,
        restaurantId = 0,
        name = "Bánh tráng trộn",
        price = 25000,
        description = "Món ăn vặt nổi tiếng",
        imagePath = null // URL giả lập
    )

    // Hiển thị màn hình chỉnh sửa
    EditDishScreen(navController = navController, dish = dish, viewModel = viewModel)
}


@Composable
fun EditDishScreen(
    navController: NavController,
    dish: Dish,
    viewModel: MenuSellerViewModel
) {
    val context = LocalContext.current

    // State quản lý thông tin món ăn
    var name by remember { mutableStateOf(dish.name) }
    var price by remember { mutableStateOf(dish.price.toString()) }
    var description by remember { mutableStateOf(dish.description ?: "") }
    var imagePath by remember { mutableStateOf(dish.imagePath ?: "") } // Đường dẫn ảnh hiện tại
    var isUploading by remember { mutableStateOf(false) } // Trạng thái upload ảnh

    // Bộ khởi chạy để mở thư viện ảnh
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            isUploading = true // Bắt đầu upload ảnh
            viewModel.updateImage(dish, selectedUri.toString()) { success ->
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
                text = "Chính sửa món ăn",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Form chỉnh sửa
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hiển thị ảnh món ăn
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Hiển thị ảnh món ăn
                if (!dish.imagePath.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(dish.imagePath)
                            .crossfade(true)
                            .size(140) // Giới hạn kích thước tải về
                            .error(R.drawable.banh_trang_tron) // Ảnh mặc định nếu tải thất bại
                            .build(),
                        contentDescription = dish.name,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val localImageName = removeDiacritics(dish.name.lowercase().replace(" ", "_"))
                    val localImageRes = getResourceId(localImageName)
                    Image(
                        painter = painterResource(id = localImageRes),
                        contentDescription = dish.name,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // Tạo khoảng cách giữa ảnh và nút

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
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("HỦY", color = Color.Black)
            }

            // Nút Lưu
            Button(
                onClick = {
                    val updatedDish = dish.copy(
                        name = name,
                        price = price.toIntOrNull() ?: dish.price,
                        description = description,
                        imagePath = imagePath
                    )
                    viewModel.updateDish(updatedDish){ success ->
                        if (success) {
                            println("Thay đổi món ăn thành công")
                        } else {
                            println("Thay đổi món ăn thất bại")
                        }
                    }
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                modifier = Modifier.weight(1f)
            ) {
                Text("LƯU THAY ĐỔI", color = Color.White)
            }
        }
    }
}


