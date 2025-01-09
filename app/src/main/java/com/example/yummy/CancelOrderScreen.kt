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

@Composable
fun CancelOrderScreen(
    orderId: Int,
    navController: NavController,
    onCancelOrder: (Int, String) -> Unit
) {
    var selectedReason by remember { mutableStateOf("") }
    var customReason by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Hủy đơn hàng",
                style = MaterialTheme.typography.titleMedium
            )
        }

        // Order ID
        Text(
            text = "Mã đơn #$orderId",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Cancel reasons
        Text(
            text = "Lý do hủy đơn",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            val reasons = listOf("Hết nguyên liệu", "Quá tải đơn hàng", "Đóng cửa sớm", "Lý do khác")
            reasons.forEach { reason ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedReason = reason },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedReason == reason,
                        onClick = { selectedReason = reason }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = reason, style = MaterialTheme.typography.bodyMedium)
                }
                if (reason == "Lý do khác" && selectedReason == "Lý do khác") {
                    TextField(
                        value = customReason,
                        onValueChange = { customReason = it },
                        placeholder = { Text("Nhập lý do cụ thể...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Confirm cancel button
        Button(
            onClick = {
                val finalReason = if (selectedReason == "Lý do khác") customReason else selectedReason
                onCancelOrder(orderId, finalReason)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Xác nhận hủy đơn", color = Color.White)
        }
    }
}
