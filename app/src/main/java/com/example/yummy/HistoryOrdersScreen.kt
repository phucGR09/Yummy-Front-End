package com.example.yummy



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController



@Composable
fun HistoryOrdersScreen(navController: NavController, viewModel: OrderSellerViewModel) {
    var isLoading by remember { mutableStateOf(true) }
    var fetchResult by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchOrders { result ->
            isLoading = false
            fetchResult = result
        }
    }

    val deliveredOrders by viewModel.delivered.collectAsState()
    val cancelledOrders by viewModel.cancelled.collectAsState()

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
                text = "Không thể tải lịch sử đơn hàng. Vui lòng thử lại.",
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
                    text = "Lịch sử đơn hàng",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // TabRow for "Đã hoàn thành" và "Đã hủy"
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
                        text = "Đã hoàn thành",
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
                        text = "Đã hủy",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Nội dung của từng tab
            when (selectedTabIndex) {
                0 -> {
                    // Nội dung "Đã hoàn thành"
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        if (deliveredOrders.isNotEmpty()) {
                            itemsIndexed(deliveredOrders) { index, order ->
                                OrderItemWithActions(
                                    orderId = order.orderId,
                                    status = "Đã hoàn thành",
                                    actions = listOf("Chi tiết"),
                                    onActionClick = { action ->
                                        if (action == "Chi tiết") {
                                            println("Xem chi tiết đơn hàng #${order.orderId}")
                                            // Điều hướng tới màn hình chi tiết đơn hàng nếu cần
                                        }
                                    }
                                )
                                if (index < deliveredOrders.size - 1) {
                                    Divider(color = Color.Gray, thickness = 1.dp)
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = "Không có đơn đã hoàn thành.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
                1 -> {
                    // Nội dung "Đã hủy"
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        if (cancelledOrders.isNotEmpty()) {
                            itemsIndexed(cancelledOrders) { index, order ->
                                OrderItemWithActions(
                                    orderId = order.orderId,
                                    status = "Đã hủy",
                                    actions = listOf("Chi tiết"),
                                    onActionClick = { action ->
                                        if (action == "Chi tiết") {
                                            println("Xem chi tiết đơn hàng #${order.orderId}")
                                            // Điều hướng tới màn hình chi tiết đơn hàng nếu cần
                                        }
                                    }
                                )
                                if (index < cancelledOrders.size - 1) {
                                    Divider(color = Color.Gray, thickness = 1.dp)
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = "Không có đơn đã hủy.",
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
