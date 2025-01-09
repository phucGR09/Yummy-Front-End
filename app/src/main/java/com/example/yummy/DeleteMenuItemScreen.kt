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
import androidx.compose.ui.text.style.TextAlign

@Preview(showBackground = true)
@Composable
fun PreviewDEditDishScreen() {
    val navController = rememberNavController()
    val viewModel = MenuSellerViewModel()

    // Món ăn giả lập để chỉnh sửa
    val dish = Dish(
        item_id =1,
        restaurant_id =0,
        name = "Bánh tráng trộn",
        price = 25000,
        description = "Món ăn vặt nổi tiếng",
        imagePath = null // URL giả lập
    )
    DeleteDishScreen(navController,dish,viewModel)
}

@Composable
fun DeleteDishScreen(
    navController: NavController,
    dish: Dish,
    viewModel: MenuSellerViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xB3000000)), // Màu nền tối với độ trong suốt
        contentAlignment = Alignment.Center
    ) {
        // Khối thông báo
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Chiếm 90% chiều rộng màn hình
                .clip(RoundedCornerShape(16.dp)) // Bo góc
                .background(Color.White) // Nền trắng cho khối thông báo
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề
            Text(
                text = "Xác Nhận Xóa",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Nội dung thông báo
            Text(
                text = "Bạn có chắc chắn muốn xóa món ăn này? Hành động này không thể hoàn tác.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Nút "XÓA MÓN ĂN"
            Button(
                onClick = {
                    viewModel.removeDish(dish.name) // Xóa món ăn
                    navController.popBackStack() // Quay lại màn hình trước
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp) // Nút bo tròn
            ) {
                Text("XÓA MÓN ĂN", color = Color.White)
            }

            // Nút "HỦY"
            OutlinedButton(
                onClick = {
                    navController.popBackStack() // Quay lại màn hình trước
                },
                colors = ButtonDefaults.outlinedButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp) // Nút bo tròn
            ) {
                Text("HỦY", color = Color.Black)
            }
        }
    }
}

