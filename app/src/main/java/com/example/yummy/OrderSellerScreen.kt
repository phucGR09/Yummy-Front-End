package com.example.yummy
import OrderModel
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
fun OrderScreen(navController: NavController, viewModel: OrderModel) {
    val preparingOrders by viewModel.preparingOrders.collectAsState()
    val completedOrders by viewModel.completedOrders.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Text(
            text = "Đơn Hàng",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color(0xFFFF5722)
        )

        // Đơn đang chuẩn bị
        Text(
            text = "Đang Chuẩn Bị",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp),
            color = Color.Gray
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(preparingOrders) { order ->
                OrderItem(order = order) {
                    // Chuyển sang chi tiết đơn hàng (sẽ thực hiện sau)
                    Toast.makeText(LocalContext.current, "Chi tiết đơn hàng: ${order.orderId}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Đơn đã hoàn thành
        Text(
            text = "Đã Hoàn Thành",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp),
            color = Color.Gray
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(completedOrders) { order ->
                OrderItem(order = order) {
                    // Chuyển sang chi tiết đơn hàng (sẽ thực hiện sau)
                    Toast.makeText(LocalContext.current, "Chi tiết đơn hàng: ${order.orderId}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
