package com.example.yummy

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
fun PreviewOrderSellerScreen() {
    val navController = rememberNavController()
    val orderModel= OrderModel()
    val viewModel = OrderSellerViewModel(orderModel)

    // Hiển thị màn hình thêm món ăn
   OrderSellerScreen(navController = navController, viewModel = viewModel)
}

@Composable
fun OrderSellerScreen(navController: NavController, viewModel: OrderSellerViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchOrders(restaurantId = 123) // Thay 123 bằng ID nhà hàng của bạn
    }
    val waitingConfirmation by viewModel.waitingConfirmation.collectAsState()
    val waitingDelivery by viewModel.waitingDelivery.collectAsState()
    val delivering by viewModel.delivering.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) } // Quản lý tab được chọn

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
                text = "Đơn hàng",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // TabRow for "Đơn hiện tại" và "Đơn mới"
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = Color(0xFFFF5722),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xFFFF5722)
                )
            }
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                selectedContentColor = Color(0xFFFF5722),
                unselectedContentColor = Color.Gray
            ) {
                Text(
                    text = "Đơn hiện tại",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                selectedContentColor = Color(0xFFFF5722),
                unselectedContentColor = Color.Gray
            ) {
                Text(
                    text = "Đơn mới",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Nội dung của từng tab
        when (selectedTabIndex) {
            0 -> {
                // Nội dung "Đơn hiện tại"
                Text(
                    text = "Đang chờ lấy hàng",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(waitingDelivery) { index, order ->
                        OrderItem(orderId = order.orderId, status = "Đang chuẩn bị")
                        if (index < waitingDelivery.size - 1) {
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                }

                // Lằn phân cách màu cam giữa các nhóm
                if (waitingDelivery.isNotEmpty() && delivering.isNotEmpty()) {
                    Divider(color = Color(0xFFFF5722), thickness = 2.dp)
                }

                Text(
                    text = "Đang giao hàng",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(delivering) { index, order ->
                        OrderItem(orderId = order.orderId, status = "Đang giao")
                        if (index < delivering.size - 1) {
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                }
            }
            1 -> {
                // Nội dung "Đơn mới"
                Text(
                    text = "Đơn mới",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    if (waitingConfirmation.isNotEmpty()) {
                        itemsIndexed(waitingConfirmation) { index, order ->
                            OrderItem(orderId = order.orderId, status = "Đang chờ xác nhận")
                            if (index < waitingConfirmation.size - 1) {
                                Divider(color = Color.Gray, thickness = 1.dp)
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = "Không có đơn mới.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItem(orderId: Int, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Đơn #$orderId", style = MaterialTheme.typography.bodyMedium)
        Text(text = status, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
    }
}


