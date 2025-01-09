package com.example.yummy // Đổi thành package của bạn

import androidx.compose.foundation.* // Đảm bảo các import cần thiết
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkSaveStateControl
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly // Đảm bảo cách đều
                    ) {
                        IconButton(onClick = { navController.navigate("HomeScreen") }) {
                            Icon(Icons.Default.Home, contentDescription = "Home", Modifier.size(30.dp))
                        }
                        IconButton(onClick = { navController.navigate("About") }) {
                            Icon(Icons.AutoMirrored.Filled.List, contentDescription = "About", Modifier.size(30.dp))
                        }
                        IconButton(onClick = { navController.navigate("Favorite") }) {
                            Icon(Icons.Default.Favorite, contentDescription = "Favorite", Modifier.size(30.dp))
                        }
                    }
                }
           },
        )
    { valuePadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 16.dp, end = 16.dp)
                .padding(valuePadding)
        ) {
            // Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, "Menu")
                    }
                    Column {
                        Text(
                            text = "Deliver to",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "41, Nguyễn Văn Cừ, P4,\n Q5, TPHCM",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    IconButton(onClick = { navController.navigate("CartScreen") }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart, // Icon giỏ hàng
                            contentDescription = "Cart",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { navController.navigate("UserProfile") }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle, // Icon giỏ hàng
                            contentDescription = "Account",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "What would you like to order?",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Search Bar
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Find for food or restaurant...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                ) {
                    // Content here - required parameter
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Food Categories
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(foodCategories) { category ->
                        FoodCategoryItem(category)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Featured Restaurants Section
                Text(
                    text = "Featured Restaurants",
                    style = MaterialTheme.typography.titleLarge
                )

                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(featuredRestaurants) { restaurant ->
                        RestaurantCard(restaurant)
                    }
                }
            }

            item {
                // recommend food
                Spacer(modifier = Modifier.height(24.dp))

                // Title for Suggested Foods
                Text(
                    text = "Suggested Foods",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
            }


            // LazyVerticalGrid for Suggested Foods
            items(suggestedFoods.chunked(2)) { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { food ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            SuggestedFoodCard(food = food, navController = navController)
                        }
                    }

                    // Nếu số lượng item lẻ, thêm một Box trống để căn chỉnh
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Các hàm và data class khác được giữ nguyên từ trước
@Composable
fun FoodCategoryItem(category: FoodCategory) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Surface(
            modifier = Modifier.size(60.dp),
            shape = MaterialTheme.shapes.medium,
            color = category.backgroundColor
        ) {
            Image(
                painter = painterResource(id = category.iconResId),
                contentDescription = category.name,
                modifier = Modifier
                    .size(30.dp)
                    .padding(15.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Image(
                painter = painterResource(id = restaurant.imageResId),
                contentDescription = restaurant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow
                        )
                        Text(text = restaurant.rating.toString())
                    }
                }
                Text(
                    text = "${restaurant.deliveryTime} mins • Free delivery",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
@Composable
fun SuggestedFoodCard(
    food: SuggestedFood,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Khi nhấn vào card, điều hướng tới màn hình chi tiết món ăn
                navController.navigate("foodDetail/${food.name}/${food.price}/${food.description}/${food.imageResId}")
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            //horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = food.imageResId),
                contentDescription = food.name,
                modifier = Modifier
                    //.size(120.dp)
                    //.aspectRatio(1f)
                    .fillMaxWidth()
                    .height(150.dp) // Chiều cao cố định cho ảnh
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center // Căn giữa nội dung
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = food.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", food.price)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

data class FoodCategory(
    val name: String,
    val iconResId: Int,
    val backgroundColor: Color
)

data class Restaurant(
    val name: String,
    val imageResId: Int,
    val rating: Double,
    val deliveryTime: Int
)

data class SuggestedFood(
    val name: String,
    val imageResId: Int,
    val price: Double,
    val description: String
)

val foodCategories = listOf(
    FoodCategory("Rice", R.drawable.rice_bowl_icon, Color(0xFFFF6B6B)),
    FoodCategory("Noodles", R.drawable.noodles_icon, Color(0xFFFFBE76)),
    FoodCategory("Beverage", R.drawable.beverage_icon, Color(0xFF4ECDC4)),
    FoodCategory("Vegetarian", R.drawable.broccoli_icon, Color(0xFFFF7F50)),
    FoodCategory("Fast Food", R.drawable.burger_icon, Color(0xFFB8E994))
)
val featuredRestaurants = listOf(
    Restaurant("McDonald's", R.drawable.mcdonalds_image, 4.5, 15),
    Restaurant("Starbucks", R.drawable.starbucks_image, 4.7, 20)
)


val suggestedFoods = listOf(
    SuggestedFood("Burger", R.drawable.burger_image, 5.99, "A hamburger, or simply a burger, is a dish consisting of fillings—usually a patty of ground meat, typically beef—placed inside a sliced bun or bread roll. \nThe patties are often served with cheese, lettuce, tomato, onion, pickles, bacon, or chilis with condiments such as ketchup, mustard, mayonnaise, relish or a \"special sauce\", often a variation of Thousand Island dressing, and are frequently placed on sesame seed buns. \nA hamburger patty topped with cheese is called a cheeseburger.\n Under some definitions, and in some cultures, a burger is considered a sandwich. \nHamburgers are typically associated with fast-food restaurants and diners but are also sold at various other restaurants, including more expensive high-end establishments. There are many international and regional variations of hamburgers. Some of the largest multinational fast-food chains feature burgers as one of their core products: McDonald's Big Mac and Burger King's Whopper have become global icons of American culture.["),
    SuggestedFood("Pizza", R.drawable.pizza_image, 8.99, "Delicious"),
    SuggestedFood("Sushi", R.drawable.sushi_image, 12.99, "Delicious"),
    SuggestedFood("Pasta", R.drawable.pasta_image, 10.99, "Delicious"),
    SuggestedFood("Salad", R.drawable.salad_image, 6.99, "Delicious"),
    SuggestedFood("Ice Cream", R.drawable.ice_cream_image, 4.50, "Delicious")
)