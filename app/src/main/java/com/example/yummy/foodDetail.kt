package com.example.yummy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetail (
    onBackClicked: () -> Unit,
    foodImage: Int,
    foodName: String,
    foodPrice: Double,
    foodDescription: String,
    cartItems: List<CartItem>, // Nhận trạng thái giỏ hàng
    updateCartItems: (List<CartItem>) -> Unit, // Hàm cập nhật trạng thái
    navController: NavController
) {
    var quantity by remember { mutableIntStateOf(1) }
    var showDialog by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chi Tiết Món Ăn") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back), // Đổi icon back phù hợp
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.width(100.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            isFavorite = !isFavorite // Đảo ngược trạng thái yêu thích
                        }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, // Chuyển đổi giữa Favorite và FavoriteBorder
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { navController.navigate("CartScreen")  }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart, // Icon giỏ hàng
                                contentDescription = "Cart",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hình ảnh món ăn
                item {

                    Image(
                        painter = painterResource(id = foodImage),
                        contentDescription = foodName,
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {


                    // Tên món ăn
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween, // Cách đều nhau
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = foodName,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f) // Chiếm không gian còn lại
                            )
                            //Spacer(modifier = Modifier.height(8.dp))

                            // Giá món ăn
                            Text(
                                text = "$${String.format(Locale.US, "%.2f", foodPrice)}",
                                fontSize = 30.sp,
                                color = Color(0xFF4CAF50), // Màu xanh lá cây
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .wrapContentWidth(Alignment.End) // Canh phải
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                item {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Nút giảm
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- } // Không giảm dưới 1
                        ) {
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = quantity.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // nút tăng
                        IconButton(
                            onClick = { quantity++ }
                        ) {
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
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    // Mô tả món ăn
                    Text(
                        text = foodDescription,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        color = Color.Gray
                    )
                }
            }
            // Nút "Add to Cart" cố định
            Button(
                onClick = {
                    val newItem = CartItem(
                        id = "${cartItems.size + 1}", // Tạo ID mới
                        name = foodName,
                        image = foodImage,
                        price = foodPrice,
                        quantity = quantity
                    )

                    // Tạo danh sách mới bằng cách thêm sản phẩm mới
                    val updatedCartItems = cartItems.toMutableList().apply { add(newItem) }
                    updateCartItems(updatedCartItems) // Cập nhật danh sách
                    // Hiển thị thông báo
                    showDialog = true
                },
                modifier = Modifier
                    .width(250.dp)
                    .height(100.dp)
                    .padding(bottom = 40.dp) // Thêm khoảng cách với đáy màn hình
                    .align(Alignment.BottomCenter), // Cố định nút ở dưới cùng
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Row(){
                    Icon(
                        imageVector = Icons.Default.Add, // Icon giỏ hàng
                        contentDescription = "Cart",
                        tint = Color.White)
                    Text(
                        text = "Add to card",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }

            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text(text = "$foodName has been added to the cart")
                    },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("OK")
                        }
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}