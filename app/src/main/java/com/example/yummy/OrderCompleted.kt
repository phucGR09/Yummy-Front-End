package com.example.yummy

import android.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun OrderCompleted(navController: NavController) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Icon dấu tích
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Order Completed",
                tint = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Text "Order Completed"
            Text(
                text = "Order Completed",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Back Home Button
            Button(
                onClick = {navController.navigate("HomeScreen")},
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                border = BorderStroke(2.dp, androidx.compose.ui.graphics.Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Back Home",
                    color = androidx.compose.ui.graphics.Color.White,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // My Orders Button
            Button(
                onClick = {navController.navigate("MyOrders")},
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "My Orders",
                    color = primaryColor
                )
            }
        }
    }
}
