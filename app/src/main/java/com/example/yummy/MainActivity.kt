package com.example.yummy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yummy.ui.theme.YummyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Áp dụng YummyTheme
            YummyTheme(darkTheme = false){
                val userAddress = "41, Nguyễn Văn Cừ, P4, Q5, TPHCM"
                val navController = rememberNavController()
                var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val isUserLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
                if (isUserLoggedIn) {
                    // Người dùng đã đăng nhập, chuyển đến màn hình chính
                    navController.navigate("HomeScreen")
                } else {
                    // Người dùng chưa đăng nhập, chuyển đến màn hình đăng nhập
                    navController.navigate("LoginScreen")
                }
                //val currentBackStackEntry by navController.currentBackStackEntryAsState()
                //val currentDestination = currentBackStackEntry?.destination?.route
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    bottomBar = {
//                        if (currentDestination in listOf("HomeScreen", "Favorite", "About")) {
//                            BottomBar(navController)
//                        }
//                    },
//                ) { paddingValues ->
                    NavHost(
                        navController,
                        startDestination = "HomeScreen",
//                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("HomeScreen") {
                            HomeScreen(navController)
                        }
                        composable(
                            "foodDetail/{foodName}/{foodPrice}/{foodDescription}/{foodImage}",
                            arguments = listOf(
                                navArgument("foodName") { type = NavType.StringType },
                                navArgument("foodPrice") { type = NavType.FloatType },
                                navArgument("foodDescription") { type = NavType.StringType },
                                navArgument("foodImage") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val foodName = backStackEntry.arguments?.getString("foodName").orEmpty()
                            val foodPrice =
                                backStackEntry.arguments?.getFloat("foodPrice")?.toDouble() ?: 0.0
                            val foodDescription =
                                backStackEntry.arguments?.getString("foodDescription").orEmpty()
                            val foodImage = backStackEntry.arguments?.getInt("foodImage")
                                ?: R.drawable.food_image

                            FoodDetail(
                                onBackClicked = { navController.popBackStack() },
                                foodImage = foodImage,
                                foodName = foodName,
                                foodPrice = foodPrice,
                                foodDescription = foodDescription,
                                cartItems = cartItems,
                                updateCartItems = { updatedItems -> cartItems = updatedItems },
                                navController
                            )
                        }
                        composable("CartScreen") {
                            CartScreen(
                                cartItems = cartItems, // Truyền giỏ hàng hiện tại
                                onUpdateCartItems = { updatedItems ->
                                    cartItems = updatedItems
                                }, // Cập nhật giỏ hàng
                                onBackClicked = { navController.popBackStack() },
                                onCheckoutClicked = {
                                    navController.navigate("CheckoutScreen")
                                }
                            )
                        }
                        composable("CheckoutScreen") {
                            CheckoutScreen(
                                cartItems = cartItems,
                                userAddress = userAddress,
                                onConfirmCheckout = {
                                    navController.navigate("OrderCompleted")
                                },
                                onBackClicked = { navController.popBackStack() }
                            )
                        }
                        composable("OrderCompleted") {
                            OrderCompleted(navController)
                        }
                        composable("Settings") {
                            Settings(navController)
                        }
                        composable("Favorite") {
                            Favorite(navController)
                        }
                        // User Profile Screen
                        composable("UserProfile") {
                            UserProfile(navController, sharedPreferences)
                        }

                        composable("EditUserProfile") {
                            EditUserProfile(navController, sharedPreferences)
                        }
                        composable("ResetPassword") {
                            ResetPassword(navController, sharedPreferences)
                        }
                        composable("MyOrders") {
                            MyOrders(
                                onBackClicked = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
               // }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    // Áp dụng YummyTheme trong preview
    YummyTheme {
        HomeScreen(navController = navController)
    }
}

data class User(
    val id: Int?,
    val userName: String,
    val email: String,
    val fullName: String?,
    val phoneNumber: String?,
    val userType: String
)