package com.example.yummy

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items // Đảm bảo import đúng hàm items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun MenuSeller(navController: NavController, viewModel: MenuSellerViewModel = MenuSellerViewModel()) {
    val categories by viewModel.categories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow), // Sửa lại để lấy đúng icon
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Thực đơn",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(48.dp)) // Chừa khoảng trống để căn giữa
        }

        // Hiển thị danh mục
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Duyệt qua từng danh mục
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onDishEdit = { dish ->
                        navController.navigate("editDish/${category.name}/${dish.name}")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút thêm danh mục
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("addCategory") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text("THÊM DANH MỤC")
            }
        }
    }
}



@Composable
fun CategoryCard(category: Category, onDishEdit: (Dish) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Tiêu đề và mô tả danh mục
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium
                )
                category.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            Text(
                text = "${category.dishes.size} Món",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Danh sách món ăn
        Column(modifier = Modifier.fillMaxWidth()) {
            category.dishes.filter { it.isVisible }.forEach { dish ->
                DishItem(dish = dish, onEditClick = { onDishEdit(dish) })
            }
        }
    }
}


@Composable
fun DishItem(dish: Dish, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Ảnh món ăn
        Image(
            painter = painterResource(id = R.drawable.banh_trang_tron), // Thay đổi tùy ảnh
            contentDescription = dish.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Thông tin món ăn
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dish.name + if (!dish.isAvailable) " (Hết hàng)" else "",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${dish.price} VNĐ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            dish.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Nút chỉnh sửa
        TextButton(onClick = onEditClick) {
            Text("Chỉnh sửa", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

