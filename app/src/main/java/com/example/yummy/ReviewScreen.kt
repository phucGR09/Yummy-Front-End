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
fun PreviewReviewScreen() {
    val navController = rememberNavController()
    val viewModel = ReviewViewModel()

    // Hiển thị màn hình thêm món ăn
    ReviewScreen(navController = navController, viewModel = viewModel)
}

@Composable
fun ReviewScreen(navController: NavController,viewModel: ReviewViewModel) {
    val reviews by viewModel.reviews.collectAsState(initial = emptyList())
    val unrespondedReviews = reviews.count { !it.isResponded }
    val averageRating = if (reviews.isNotEmpty()) reviews.map { it.rating }.average() else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                text = "Đánh giá khách hàng",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(48.dp))
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Tổng quan
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Điểm Trung Bình", style = MaterialTheme.typography.titleMedium)
                Text(String.format("%.1f", averageRating), style = MaterialTheme.typography.displayMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Chưa phản hồi", style = MaterialTheme.typography.titleMedium)
                Text(unrespondedReviews.toString(), style = MaterialTheme.typography.displayMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách đánh giá
        LazyColumn {
            items(reviews) { review ->
                ReviewItem(review)
                Divider(modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFFFF5722) // Màu cam

                )
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Đơn hàng #${review.orderId}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = if (review.isResponded) "Đã phản hồi" else "Chưa phản hồi",
                color = if (review.isResponded) Color.Green else Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.comment,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Điểm: ${review.rating}★", style = MaterialTheme.typography.bodyMedium)
            Text("Thời gian: ${review.reviewTime}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
