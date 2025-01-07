package com.example.yummy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yummy.ui.theme.YummyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Áp dụng YummyTheme
            YummyTheme(darkTheme = false){
                val navController = rememberNavController()
                var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }
                NavHost(
                    navController,
                    startDestination = "HomeScreen",
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
                        val foodPrice = backStackEntry.arguments?.getFloat("foodPrice")?.toDouble() ?: 0.0
                        val foodDescription = backStackEntry.arguments?.getString("foodDescription").orEmpty()
                        val foodImage = backStackEntry.arguments?.getInt("foodImage") ?: R.drawable.food_image

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
                            onUpdateCartItems = { updatedItems -> cartItems = updatedItems }, // Cập nhật giỏ hàng
                            onBackClicked = { navController.popBackStack() },
                            onCheckoutClicked = {
                                navController.navigate("CheckoutScreen")
                            }
                        )
                    }
                }
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
