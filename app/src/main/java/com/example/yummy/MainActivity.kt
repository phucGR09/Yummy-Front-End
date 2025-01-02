package com.example.yummy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "HomeScreen", builder = {
                composable(
                    "HomeScreen"){
                    HomeScreen(navController)
                }
                composable(
                    "foodDetail/{foodName}/{foodPrice}/{foodDescription}/{foodImage}",
                    arguments = listOf(
                        navArgument("foodName") { type = NavType.StringType},
                        navArgument("foodPrice") { type = NavType.StringType },
                        navArgument("foodDescription") { type = NavType.StringType },
                        navArgument("foodImage") { type = NavType.IntType }
                    )
                ){ backStackEntry ->
                    val foodName = backStackEntry.arguments?.getString("foodName").orEmpty()
                    val foodPrice = backStackEntry.arguments?.getString("foodPrice").orEmpty()
                    val foodDescription = backStackEntry.arguments?.getString("foodDescription").orEmpty()
                    val foodImage = backStackEntry.arguments?.getInt("foodImage") ?: R.drawable.food_image

                    FoodDetail(
                        onBackClicked = { navController.popBackStack() },
                        foodImage = foodImage,
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription
                    )
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
