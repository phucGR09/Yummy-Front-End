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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetail(
    foodItemId: Int,
    foodName: String,
    foodPrice: Int,
    foodDescription: String,
    foodImagePath: String,
    onBackClicked: () -> Unit,
    navController: NavController
) {
    // Convert image path to a drawable resource (if applicable)

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
                            painter = painterResource(id = R.drawable.ic_back),
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
                            isFavorite = !isFavorite // Toggle favorite state
                        }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { navController.navigate("CartScreen") }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
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
            ) {
                // Image
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(foodImagePath) // URL of the image
                            .crossfade(true) // Optional: adds smooth transition effect
                            .build(),
                        contentDescription = foodName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Fixed height for the image
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Food details
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = foodName,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f)
                            )

                            // Price
                            Text(
                                text = "$foodPrice$",
                                fontSize = 30.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.End,
                                modifier = Modifier.wrapContentWidth(Alignment.End)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                // Quantity control
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Decrease button
                        IconButton(onClick = { if (quantity > 1) quantity-- }) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
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

                        // Increase button
                        IconButton(onClick = { quantity++ }) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Increase quantity",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Description
                item {
                    Text(
                        text = foodDescription,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        color = Color.Gray
                    )
                }
            }

            // Add to Cart button
            Button(
                onClick = {

                },
                modifier = Modifier
                    .width(250.dp)
                    .height(100.dp)
                    .padding(bottom = 40.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Row() {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Cart",
                        tint = Color.White
                    )
                    Text(
                        text = "Add to card",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }

            // Dialog after adding to cart
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text(text = "$foodName has been added to the cart")
                    },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("OK")
                        }
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
