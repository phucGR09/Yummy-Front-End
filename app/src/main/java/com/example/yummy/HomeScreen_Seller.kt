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
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color(0xFFFFF2EE), // Màu nền cam nhạt
        contentColor = Color.Black
    ) {
        val items = listOf(
            NavigationItem("Trang chủ", R.drawable.icon_home, "home_screen"),
            NavigationItem("Chat", R.drawable.icon_chat, "chat_screen"),
            NavigationItem("Cửa hàng", R.drawable.icon_store, "store_screen"),
            NavigationItem("Thông báo", R.drawable.icon_notifications, "notification_screen")
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title, style = MaterialTheme.typography.labelSmall) },
                selected = false, // Thay bằng logic chọn dựa trên route hiện tại
                onClick = {
                    navController.navigate(item.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFF5722), // Màu cam khi được chọn
                    unselectedIconColor = Color.Gray, // Màu xám khi không được chọn
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class NavigationItem(val title: String, val icon: Int, val route: String)


@Composable
fun StoreHomeScreen(navController: NavController, viewModel: StoreViewModel) {
    val storeInfo = viewModel.storeInfo
    val products by viewModel.products.collectAsState()
    val isProductListEmpty by viewModel.isProductListEmpty.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    Triple("Phản hồi", R.drawable.feedback_icon, "customer_reviews"),
                    Triple("Thực đơn", R.drawable.menu_icon, "menu"),
                    Triple("Đơn hàng", R.drawable.order_icon, "orders"),
                    Triple(
                        "Lịch sử",
                        R.drawable.history_icon,
                        "history_orders"
                    ), // Thay đổi nếu có màn hình lịch sử riêng
                    Triple("Doanh thu", R.drawable.revenue_icon, "revenue")
                )
                functions.forEach { (name, icon, route) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate(route) // Điều hướng đến trang tương ứng
                            }
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
