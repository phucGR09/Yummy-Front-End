package com.example.yummy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview(showBackground = true)
@Composable
fun PreviewRevenueSellerScreen() {
    val navController = rememberNavController()
    val orderModel= OrderModel()
    val viewModel = AvenueSellerViewModel(orderModel)

    // Hiển thị màn hình thêm món ăn
    AvenueSellerScreen(navController = navController, viewModel = viewModel)
}

@Composable
fun AvenueSellerScreen(navController: NavController,viewModel: AvenueSellerViewModel) {
    val totalRevenue by viewModel.totalRevenue.collectAsState(initial = 0.0)
    val totalCompletedOrders by viewModel.totalCompletedOrders.collectAsState(initial = 0)
    val averageRevenuePerOrder by viewModel.averageRevenuePerOrder.collectAsState(initial = 0.0)

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFFFF5722),
                modifier = Modifier
                    .size(23.dp)
                    .clickable { navController.popBackStack() }
            )

            Text(
                text = "Doanh thu",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(48.dp))
        }
        Text(
            text = "Tổng doanh thu",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${"%,.0f".format(totalRevenue)}đ", // Định dạng số tiền
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Số đơn hoàn thành: $totalCompletedOrders",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Giá trị trung bình / đơn: ${"%,.0f".format(averageRevenuePerOrder)}đ",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
