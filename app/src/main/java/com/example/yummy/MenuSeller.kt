package com.example.yummy

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

fun removeDiacritics(input: String): String {
    val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(normalized).replaceAll("").replace("đ", "d").replace("Đ", "D")
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuHomeScreen() {
    val orderModel= MenuModel()
    val viewModel = MenuSellerViewModel(orderModel)
    val navController = rememberNavController()
    MenuSeller(navController = navController,viewModel)
}

@Composable
fun MenuSeller(navController: NavController, viewModel: MenuSellerViewModel) {
    val dishes by viewModel.dishes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top Bar (luôn cố định ở trên)
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
                text = "Thực đơn",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Danh sách món ăn (cuộn được)
        LazyColumn(
            modifier = Modifier
                .weight(1f) // Chiếm toàn bộ không gian còn lại
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dishes) { dish ->
                DishItem(
                    dish = dish,
                    onEditClick = { navController.navigate("editDish/${dish.name}") },
                    onDeleteClick = { navController.navigate("deleteDish/${dish.name}") }
                )
            }
        }

        // Nút thêm món ăn (luôn cố định ở dưới)
        Button(
            onClick = { navController.navigate("addDish") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("THÊM MÓN ĂN", color = Color.White)
        }
    }
}

@Composable
fun DishItem(dish: Dish, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFFE724C), RoundedCornerShape(8.dp)) // Viền màu cam
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Hiển thị ảnh món ăn
            if (!dish.imagePath.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(dish.imagePath)
                        .crossfade(true)
                        .size(80) // Giới hạn kích thước tải về
                        .error(R.drawable.banh_trang_tron) // Ảnh mặc định nếu tải thất bại
                        .build(),
                    contentDescription = dish.name,
                    modifier = Modifier
                        .size(80.dp)
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
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin món ăn
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dish.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${dish.price} VNĐ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black // Giá tiền màu đen
                )
                dish.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nút chỉnh sửa và xóa
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onEditClick) {
                Text("Chỉnh sửa", color = Color(0xFFFF5722)) // Nút chỉnh sửa màu cam
            }
            TextButton(onClick = onDeleteClick) {
                Text("Xóa", color = Color.Red) // Nút xóa màu đỏ
            }
        }
    }
}


// Hàm lấy Resource ID cho ảnh tĩnh
@Composable
fun getResourceId(imageName: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}
