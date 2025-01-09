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

@Preview(showBackground = true)
@Composable
fun PreviewStoreHomeScreen() {
    val navController = rememberNavController()
    StoreHomeScreen(navController = navController)
}

@Composable
fun StoreHomeScreen(navController: NavController, viewModel: StoreViewModel = StoreViewModel()) {
    val storeInfo = viewModel.storeInfo
    val products by viewModel.products.collectAsState()
    val isProductListEmpty by viewModel.isProductListEmpty.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFFFF5722) // Màu cam
                )
            }
            Column {
                Text(
                    text = storeInfo.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Image(
                painter = painterResource(id = R.drawable.icon_avatar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }

        // Logo cửa hàng và thông tin
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = storeInfo.logoResId),
                contentDescription = "KeyBox Kafe Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = storeInfo.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = storeInfo.address,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // 5 nút chức năng
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            val functions = listOf(
                Pair("Phản hồi", R.drawable.feedback_icon),
                Pair("Thực đơn", R.drawable.menu_icon),
                Pair("Đơn hàng", R.drawable.order_icon),
                Pair("Lịch sử", R.drawable.history_icon),
                Pair("Doanh thu", R.drawable.revenue_icon)
            )
            functions.forEach { (name, icon) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = name,
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFFFF5722) // Màu cam
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Sản phẩm bán chạy
        Text(
            text = "Sản phẩm bán chạy",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Hiển thị danh sách sản phẩm
        if (isProductListEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không có sản phẩm nào.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    ProductCard(product = product)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                // Xử lý khi người dùng nhấn vào sản phẩm
                println("Sản phẩm: ${product.name}")
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE5D1)), // Màu cam nhạt
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${product.price} VNĐ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFF5722) // Màu cam
                )
            }
        }
    }
}
