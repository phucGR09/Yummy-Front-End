package com.example.yummy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetail (
    onBackClicked: () -> Unit,
    foodImage: Int,
    foodName: String,
    foodPrice: String,
    foodDescription: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chi Tiết Món Ăn") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back), // Đổi icon back phù hợp
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hình ảnh món ăn
            Image(
                painter = painterResource(id = foodImage),
                contentDescription = foodName,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tên món ăn
            Text(
                text = foodName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Giá món ăn
            Text(
                text = foodPrice,
                fontSize = 20.sp,
                color = Color(0xFF4CAF50), // Màu xanh lá cây
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Mô tả món ăn
            Text(
                text = foodDescription,
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                color = Color.Gray
            )
        }
    }
}