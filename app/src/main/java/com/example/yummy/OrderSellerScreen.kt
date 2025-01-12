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
fun PreviewOrderSellerScreen() {
    val navController = rememberNavController()
    val orderModel= OrderModel()
    val viewModel = OrderSellerViewModel(orderModel)

    // Hiển thị màn hình thêm món ăn
   OrderSellerScreen(navController = navController, viewModel = viewModel)
}

@Composable
fun OrderSellerScreen(navController: NavController, viewModel: OrderSellerViewModel) {
    var isLoading by remember { mutableStateOf(true) }
    var fetchResult by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        viewModel.fetchOrders { result ->
            isLoading = false
            fetchResult = result
        }
    }
    val waitingConfirmation by viewModel.waitingConfirmation.collectAsState()
    val delivering by viewModel.delivering.collectAsState()
    val confirmed by viewModel.confirmed.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) } // Quản lý tab được chọn
    if (isLoading) {
        // Hiển thị Loading Spinner
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (!fetchResult) {
        // Hiển thị thông báo lỗi
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Không thể tải đơn hàng. Vui lòng thử lại.",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
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
                        text = "Đang chuẩn bị",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(confirmed) { index, order ->
                            OrderItemWithActions(
                                orderId = order.orderId,
                                status = "Đang chuẩn bị",
                                actions = listOf("Liên hệ", "Hủy đơn"),
                                onActionClick = { action ->
                                    if (action == "Hủy đơn") {
                                        navController.navigate("cancel_order/${order.orderId}")
                                    }
                                }
                            )
                            if (index < confirmed.size - 1) {
                                Divider(color = Color.Gray, thickness = 1.dp)
                            }
                        }
                    }

                    if (confirmed.isNotEmpty() && delivering.isNotEmpty()) {
                        Divider(color = Color(0xFFFF5722), thickness = 2.dp)
                    }

                    Text(
                        text = "Đang giao hàng",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(delivering) { index, order ->
                            OrderItemWithActions(
                                orderId = order.orderId,
                                status = "Đang giao",
                                actions = listOf("Liên hệ", "Theo dõi đơn hàng"),
                                onActionClick = { action ->
                                    // Điều hướng đến trang liên hệ hoặc theo dõi đơn hàng
                                }
                            )
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
                                OrderItemWithActions(
                                    orderId = order.orderId,
                                    status = "Đang chờ xác nhận",
                                    actions = listOf("Từ chối đơn hàng", "Xác nhận đơn hàng"),
                                    onActionClick = { action ->
                                        when (action) {
                                            "Từ chối đơn hàng" -> {
                                                viewModel.updateOrderStatus(order, OrderStatus.CANCELLED) { success ->
                                                    if (success) {
                                                        println("Đơn hàng #${order.orderId} đã bị từ chối.")
                                                    } else {
                                                        println("Không thể từ chối đơn hàng #${order.orderId}.")
                                                    }
                                                }
                                            }
                                            "Xác nhận đơn hàng" -> {
                                                viewModel.updateOrderStatus(order, OrderStatus.RESTAURANT_CONFIRMED) { success ->
                                                    if (success) {
                                                        println("Đơn hàng #${order.orderId} đã được xác nhận.")
                                                    } else {
                                                        println("Không thể xác nhận đơn hàng #${order.orderId}.")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
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
}
@Composable
fun OrderItemWithActions(
    orderId: Int,
    status: String,
    actions: List<String>,
    onActionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Đơn #$orderId", style = MaterialTheme.typography.bodyMedium)
            Text(text = status, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        }

        // Thêm các nút hành động
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            actions.forEachIndexed { index, action ->
                Button(
                    onClick = { onActionClick(action) },
                    colors = when (action) {
                        "Liên hệ", "Từ chối đơn hàng" -> ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                        "Hủy đơn", "Xác nhận đơn hàng", "Theo dõi đơn hàng" -> ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5722),
                            contentColor = Color.White
                        )
                        else -> ButtonDefaults.buttonColors()
                    },
                    border = when (action) {
                        "Liên hệ", "Từ chối đơn hàng" -> BorderStroke(1.dp, Color.Gray)
                        else -> null
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (index < actions.size - 1) 8.dp else 0.dp)
                ) {
                    Text(text = action, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}





