package com.example.yummy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

@Composable
fun SearchDishScreen(menuModel: MenuModel, onBack: () -> Unit) {
    val searchQuery = remember { mutableStateOf("") }
    val dishes by menuModel.dishes.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(16.dp).padding(top = 20.dp)) {
        // Nút "Back"
        androidx.compose.material3.Button(
            onClick = onBack, // Gọi hàm điều hướng trở về
            modifier = Modifier.height(40.dp) // Chiều cao nút nhỏ gọn
        ) {
            Text("Back")
        }

        // Row chứa TextField và Button "Find"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // TextField để nhập nội dung tìm kiếm
            TextField(
                value = searchQuery.value,
                onValueChange = { newQuery -> searchQuery.value = newQuery },
                label = { Text("Tìm kiếm món ăn") },
                modifier = Modifier.weight(1f) // Chiếm toàn bộ không gian trống còn lại
            )

            Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa TextField và Button

            // Button "Find"
            androidx.compose.material3.Button(
                onClick = {
                    // Chỉ thực hiện tìm kiếm khi nhấn vào nút "Find"
                    if (searchQuery.value.isNotEmpty()) {
                        coroutineScope.launch {
                            menuModel.searchDishByName(searchQuery.value)
                        }
                    }
                },
                modifier = Modifier
                    .height(56.dp) // Cùng chiều cao với TextField
            ) {
                Text("Find")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị danh sách món ăn tìm được
        LazyColumn {
            items(dishes) { dish ->
                DishRow(dish = dish)
            }
        }
    }
}
@Composable
fun DishRow(dish: Dish) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Image
        Image(
            painter = rememberAsyncImagePainter(dish.imagePath),
            contentDescription = dish.name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text: Name and Price
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = dish.name,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${dish.price} $",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}