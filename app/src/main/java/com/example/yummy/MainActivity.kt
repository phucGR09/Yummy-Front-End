package com.example.yummy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yummy.ui.theme.YummyTheme

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val menuModel= MenuModel()
//
//        val viewModel = MenuSellerViewModel(menuModel) // Khởi tạo ViewModel
//
//        setContent {
//            YummyTheme {
//                val navController = rememberNavController()
//                AppNavGraph(navController = navController, viewModel = viewModel)
//            }
//        }
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YummyTheme(darkTheme = false) {
                val navController = rememberNavController()
                var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val userAddress = "41, Nguyễn Văn Cừ, P4, Q5, TPHCM"

                NavHost(
                    navController,
                    startDestination = "welcome_screen",
                ) {
                    composable("welcome_screen") {
                        WelcomeScreen(
                            onSignUpClick = { navController.navigate("sign_up_screen") },
                            onSignInClick = { navController.navigate("sign_in_screen") }
                        )
                    }
                    composable("sign_up_screen") {
                        SignUpScreen(
                            onSignUpSuccess = { name, contactInfo, password, role ->
                                val generatedOtp = generateOtp()
                                navController.navigate("verification_code_screen/$name/$contactInfo/$password/$role/$generatedOtp")
                            },
                            onSignInClick = { navController.navigate("sign_in_screen") }
                        )
                    }
                    composable(
                        "verification_code_screen/{name}/{contactInfo}/{password}/{role}/{generatedOtp}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("contactInfo") { type = NavType.StringType },
                            navArgument("password") { type = NavType.StringType },
                            navArgument("role") { type = NavType.StringType },
                            navArgument("generatedOtp") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val contactInfo = backStackEntry.arguments?.getString("contactInfo") ?: ""
                        val password = backStackEntry.arguments?.getString("password") ?: ""
                        val role = backStackEntry.arguments?.getString("role") ?: ""
                        val generatedOtp = backStackEntry.arguments?.getString("generatedOtp") ?: ""

                        VerificationCodeScreen(
                            name = name,
                            contactInfo = contactInfo,
                            password = password,
                            role = role,
                            generatedOtp = generatedOtp,
                            onVerifySuccess = {
                                saveUserDetails(
                                    context = this@MainActivity,
                                    name = name,
                                    emailOrPhone = contactInfo,
                                    password = password,
                                    role = role
                                )
                                Toast.makeText(
                                    this@MainActivity,
                                    "User details saved successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (role.trim().equals("buyer", ignoreCase = true)) {
                                    navController.navigate("profile_buyer_screen/$name/$contactInfo")
                                } else if (role.trim().equals("seller", ignoreCase = true)) {
                                    navController.navigate("profile_seller_screen/$name/$contactInfo")
                                } else {
                                    navController.navigate("profile_shipper_screen/$name/$contactInfo")
                                }
                            },
                            onResendOtp = {
                                val newOtp = generateOtp()
                                Toast.makeText(
                                    this@MainActivity,
                                    "OTP resent to ${if (contactInfo.contains("@")) "email" else "phone"}: $contactInfo. OTP: $newOtp",
                                    Toast.LENGTH_SHORT
                                ).show()
                                newOtp
                            }
                        )
                    }
                    composable(
                        "profile_shipper_screen/{name}/{contactInfo}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("contactInfo") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val contactInfo = backStackEntry.arguments?.getString("contactInfo") ?: ""
                        ProfileShipperScreen(
                            name = name,
                            contactInfo = contactInfo,
                            onSave = { updatedName, updatedAddress ->
                                saveUserDetails(
                                    context = this@MainActivity,
                                    name = updatedName,
                                    emailOrPhone = contactInfo,
                                    password = "",
                                    role = "shipper"
                                )
                                navController.navigate("home_screen")
                            }
                        )
                    }
                    composable(
                        "profile_buyer_screen/{name}/{contactInfo}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("contactInfo") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val contactInfo = backStackEntry.arguments?.getString("contactInfo") ?: ""
                        ProfileBuyerScreen(
                            name = name,
                            contactInfo = contactInfo,
                            onSave = { updatedName, updatedAddress ->
                                saveUserDetails(
                                    context = this@MainActivity,
                                    name = updatedName,
                                    emailOrPhone = contactInfo,
                                    password = "",
                                    role = "buyer"
                                )
                                Toast.makeText(
                                    this@MainActivity,
                                    "Profile updated successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("home_screen")
                            }
                        )
                    }
                    composable(
                        "profile_seller_screen/{name}/{contactInfo}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("contactInfo") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val contactInfo = backStackEntry.arguments?.getString("contactInfo") ?: ""
                        ProfileSellerScreen(
                            name = name,
                            contactInfo = contactInfo,
                            onSave = { updatedName, updatedStoreName ->
                                saveUserDetails(
                                    context = this@MainActivity,
                                    name = updatedName,
                                    emailOrPhone = contactInfo,
                                    password = "",
                                    role = "seller"
                                )
                                navController.navigate("home_screen")
                            }
                        )
                    }
                    composable("home_screen") {
                        HomeScreen(navController = navController)
                    }
                    composable("CartScreen") {
                        CartScreen(
                            cartItems = cartItems,
                            onUpdateCartItems = { updatedItems -> cartItems = updatedItems },
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
                }
            }
        }
    }

    private fun generateOtp(): String {
        return (1000..9999).random().toString()
    }

    private fun saveUserDetails(
        context: Context,
        name: String,
        emailOrPhone: String,
        password: String,
        role: String
    ) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("emailOrPhone", emailOrPhone)
        editor.putString("password", password)
        editor.putString("role", role)
        editor.apply()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    YummyTheme {
        HomeScreen(navController = navController)
    }
}



//@Composable
//fun AppNavGraph(navController: NavHostController, viewModel: MenuSellerViewModel) {
//    NavHost(navController = navController, startDestination = "menu_seller_screen") {
//        composable("menu_seller_screen") {
//            MenuSeller(
//                navController = navController,
//                viewModel = viewModel
//            )
//        }
//
//        composable("addDish") {
//            AddDishScreen(navController = navController,viewModel)
//        }
//
//        composable("editDish/{dishName}") { backStackEntry ->
//            val dishName = backStackEntry.arguments?.getString("dishName") ?: ""
//            val dish = viewModel.dishes.collectAsState().value.find { it.name == dishName }
//            if (dish != null) {
//                EditDishScreen(navController = navController, dish = dish, viewModel)
//            }
//        }
//
//        composable("deleteDish/{dishName}") { backStackEntry ->
//            val dishName = backStackEntry.arguments?.getString("dishName") ?: ""
//            val dish = viewModel.dishes.collectAsState().value.find { it.name == dishName }
//            if (dish != null) {
//                DeleteDishScreen(
//                    navController = navController,
//                    dish = dish,
//                    viewModel = viewModel
//                )
//            }
//        }
//    }
//}
