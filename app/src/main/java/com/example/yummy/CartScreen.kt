package com.example.yummy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItem>, // Nhận danh sách từ cha hoặc ViewModel
    onUpdateCartItems: (List<CartItem>) -> Unit, // Cập nhật danh sách khi có thay đổi
    onCheckoutClicked: () -> Unit,
    onBackClicked: () -> Unit) {
    // Fake data for cart items

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giỏ hàng") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
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
        ) {
            if (cartItems.isEmpty()) {
                // Hiển thị khi giỏ hàng trống
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.empty_image),
                        contentDescription = "empty cart",
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cart is empty",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onRemove = { id ->
                                val updatedItems = cartItems.filter { it.id != id }
                                onUpdateCartItems(updatedItems) // Gọi hàm cập nhật danh sách
                            },
                            onQuantityChange = { id, quantity ->
                                val updatedItems = cartItems.map {
                                    if (it.id == id) it.copy(quantity = quantity) else it
                                }
                                onUpdateCartItems(updatedItems) // Gọi hàm cập nhật danh sách
                            }
                        )
                    }
                }

                // Total price and checkout button
                val totalPrice = cartItems.sumOf { it.price * it.quantity }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tổng: $${String.format(Locale.ROOT, "%.2f", totalPrice)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onCheckoutClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Thanh toán", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onRemove: (String) -> Unit,
    onQuantityChange: (String, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = item.name,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 16.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "$${String.format(Locale.US, "%.2f", item.price)} x ${item.quantity}",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                if (item.quantity > 1) onQuantityChange(item.id, item.quantity - 1)
            }) {
                Surface(
                    shape = CircleShape, // Hình tròn
                    color = MaterialTheme.colorScheme.primary, // Màu từ theme
                    modifier = Modifier.size(30.dp) // Kích thước hình tròn
                ) {
                    Box(
                        contentAlignment = Alignment.Center // Đặt Icon vào giữa
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown, // Icon giỏ hàng
                            contentDescription = "Decrease quantity",
                            tint = Color.White
                        )
                    }
                }
            }
            Text(
                text = "${item.quantity}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { onQuantityChange(item.id, item.quantity + 1) }) {
                Surface(
                    shape = CircleShape, // Hình tròn
                    color = MaterialTheme.colorScheme.primary, // Màu từ theme
                    modifier = Modifier.size(30.dp) // Kích thước hình tròn
                ) {
                    Box(
                        contentAlignment = Alignment.Center // Đặt Icon vào giữa
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp, // Icon giỏ hàng
                            contentDescription = "Increase quantity",
                            tint = Color.White
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { onRemove(item.id) }) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Remove item",
                tint = Color.Red
            )
        }
    }
}

data class CartItem(
    val id: String,
    val name: String,
    val image: Int,
    val price: Double,
    var quantity: Int,
)
