package com.example.yummy

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
fun CheckoutScreen(
    cartItems: List<CartItem>,
    userAddress: String,
    onConfirmCheckout: () -> Unit,
    onBackClicked: () -> Unit
) {
    var customerName by remember { mutableStateOf("Phuc") }
    var address by remember { mutableStateOf(userAddress) }
    var selectedPaymentMethod by remember { mutableStateOf("Thanh toán khi nhận hàng") }
    var discountCode by remember { mutableStateOf("") }
    var discountAmount by remember { mutableDoubleStateOf(0.0) }
    var userPhone by remember { mutableStateOf("0832544365") }
    val totalItemPrice = cartItems.sumOf { it.price * it.quantity }
    val shippingCost = 5.0
    val totalPayment = totalItemPrice + shippingCost - discountAmount

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh toán") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            //verticalArrangement = Arrangement.spacedBy(.dp)
        ) {
            // Customer Name
            item {
                Row (
                    modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Person, // Icon giỏ hàng
                        contentDescription = "user",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Tên người đặt hàng") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent, // Tắt viền khi focus
                            unfocusedBorderColor = Color.Transparent // Tắt viền khi không focus
                        )
                    )
                }

            }

            // Address
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn, // Icon giỏ hàng
                        contentDescription = "Location",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Địa chỉ giao hàng") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent, // Tắt viền khi focus
                            unfocusedBorderColor = Color.Transparent // Tắt viền khi không focus
                        )
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Call, // Icon điện thoại
                        contentDescription = "phone",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = userPhone, // Giá trị của số điện thoại
                        onValueChange = { userPhone = it },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent, // Tắt viền khi focus
                            unfocusedBorderColor = Color.Transparent // Tắt viền khi không focus
                        )
                    )
                }
            }

            // List of ordered items
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Danh sách món đã đặt",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            items(cartItems) { item ->
                CheckoutCartItem(item = item)
            }

            // Payment Methods
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Phương thức thanh toán",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            item {
                Column {
                    val paymentMethods = listOf(
                        "Chuyển khoản ngân hàng",
                        "Master Card",
                        "Thanh toán khi nhận hàng"
                    )
                    paymentMethods.forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .selectable(
                                    selected = (selectedPaymentMethod == method),
                                    onClick = { selectedPaymentMethod = method }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedPaymentMethod == method),
                                onClick = { selectedPaymentMethod = method }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = method,
                                fontWeight = if (selectedPaymentMethod == method) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Discount Code
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = discountCode,
                        onValueChange = { discountCode = it },
                        label = { Text("Mã giảm giá") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            // Fake logic for discount
                            discountAmount = if (discountCode == "DISCOUNT10") 10.0 else 0.0
                        }
                    ) {
                        Text("Apply")
                    }
                }
            }

            // Invoice
            item {
                Text(
                    text = "Hóa đơn",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    InvoiceRow(label = "Tổng tiền món:", value = totalItemPrice)
                    InvoiceRow(label = "Phí ship:", value = shippingCost)
                    InvoiceRow(label = "Giảm giá:", value = -discountAmount)
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                    InvoiceRow(label = "Tổng thanh toán:", value = totalPayment, bold = true)
                }
            }

            // Confirm Button
            item {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onConfirmCheckout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp) // Set icon size
                        )
                        Text("Completed", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutCartItem(item: CartItem) {
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
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                text = "Số lượng: ${item.quantity} - Giá: $${String.format(Locale.US, "%.2f", item.price)}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun InvoiceRow(label: String, value: Double, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )
        Text(
            text = "$${String.format(Locale.US, "%.2f", value)}",
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}
