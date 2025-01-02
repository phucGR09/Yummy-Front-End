package com.example.yummy // Đổi thành package của bạn

import android.content.ClipDescription
import androidx.compose.foundation.* // Đảm bảo các import cần thiết
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top Bar
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        text = "4102 Pretty View Lane",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.small)
                )
            }
        }
        item{
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
        items(suggestedFoods.chunked(2)) { rowFoods ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowFoods.forEach { food ->
                    SuggestedFoodCard(
                        food = food,
                        navController = navController,
                        modifier = Modifier.weight(1f) // This ensures each card takes up half the width
                    )
                }
                if (rowFoods.size < 2) {
                    Spacer(modifier = Modifier.weight(1f)) // Spacing to ensure proper alignment if only one item in the row
                }
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
    modifier: Modifier = Modifier,
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
                        text = food.price,
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
    val price: String,
    val description: String
)

val foodCategories = listOf(
    FoodCategory("Burger", R.drawable.ic_user, Color(0xFFFF6B6B)),
    FoodCategory("Donut", R.drawable.ic_fastfood, Color(0xFFFFBE76)),
    FoodCategory("Pizza", R.drawable.ic_pizza, Color(0xFF4ECDC4)),
    FoodCategory("Mexican", R.drawable.ic_hamburger, Color(0xFFFF7F50)),
    FoodCategory("Asian", R.drawable.ic_tea, Color(0xFFB8E994))
)
val featuredRestaurants = listOf(
    Restaurant("McDonald's", R.drawable.mcdonalds_image, 4.5, 15),
    Restaurant("Starbucks", R.drawable.starbucks_image, 4.7, 20)
)


val suggestedFoods = listOf(
    SuggestedFood("Burger", R.drawable.burger_image, "$5.99", "Delicious"),
    SuggestedFood("Pizza", R.drawable.pizza_image, "$8.99", "Delicious"),
    SuggestedFood("Sushi", R.drawable.sushi_image, "$12.99", "Delicious"),
    SuggestedFood("Pasta", R.drawable.pasta_image, "$10.99", "Delicious"),
    SuggestedFood("Salad", R.drawable.salad_image, "$6.99", "Delicious"),
    SuggestedFood("Ice Cream", R.drawable.ice_cream_image, "$4.50", "Delicious")
)
