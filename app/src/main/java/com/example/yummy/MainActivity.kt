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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yummy.ui.theme.YummyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response





class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val menuModel = MenuModel()
        // Sử dụng lifecycleScope để đảm bảo an toàn với vòng đời Activity
        lifecycleScope.launch {
            val success = menuModel.getDishes()
            if (success) {
                println("Danh sách món ăn được tải thành công khi khởi động ứng dụng.")
            } else {
                println("Không thể tải danh sách món ăn khi khởi động.")
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            YummyTheme(darkTheme = false) {
                val navController = rememberNavController()
                var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val userAddress = "41, Nguyễn Văn Cừ, P4, Q5, TPHCM"
                val storeviewModel = StoreViewModel()
                val reviewviewModel = ReviewViewModel()
                val orderModel = OrderModel()
                val MenuSellerviewModel = MenuSellerViewModel(menuModel)
                val OrderSellerviewModel = OrderSellerViewModel(orderModel)
                val AvenueSellerviewModel = AvenueSellerViewModel(orderModel)
                NavHost(
                    navController,
                    startDestination = "WelcomeActivity",
                ) {
                    composable("WelcomeActivity") {
                        WelcomeScreen(
                            onSignUpClick = { navController.navigate("SignUpScreen") },
                            onSignInClick = { navController.navigate("SignInScreen") }
                        )
                    }
                    composable("SignInScreen") {
                        SignInScreen(
                            onSignInSuccess = { userType ->
                                when (userType) {
                                    "CUSTOMER" -> {
                                        navController.navigate("HomeScreen") {
                                            popUpTo("SignInScreen") { inclusive = true }
                                        }
                                    }
                                    "RESTAURANT_OWNER" -> {
                                        navController.navigate("store_home") {
                                            popUpTo("SignInScreen") { inclusive = true }
                                        }
                                    }
                                    "DELIVERY_DRIVER" -> {
                                        navController.navigate("HomeScreen") {
                                            popUpTo("SignInScreen") { inclusive = true }
                                        }
                                    }
                                    else -> {
                                        Toast.makeText(this@MainActivity, "Vai trò không xác định!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onSignUpClick = {
                                // Điều hướng đến màn hình đăng ký
                                navController.navigate("SignUpScreen")
                            }
                        )
                    }
                    composable("SignUpScreen"){
                        SignUpScreen(
                            onSignUpSuccess = {
                                navController.navigate("SignInScreen")
                            },
                            onSignInClick ={
                                navController.navigate("SignInScreen")
                            }
                        )
                    }
                    composable(
                        "ProfileBuyerScreen/{username}/{fullName}/{email}/{phoneNumber}/{address}",
                        arguments = listOf(
                            navArgument("username") { type = NavType.StringType },
                            navArgument("fullName") { type = NavType.StringType },
                            navArgument("email") { type = NavType.StringType },
                            navArgument("phoneNumber") { type = NavType.StringType },
                            navArgument("address") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        val fullName = backStackEntry.arguments?.getString("fullName") ?: ""
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
                        val address = backStackEntry.arguments?.getString("address") ?: ""

                        ProfileBuyerScreen(
                            fullName = fullName,
                            username = username,
                            email = email,
                            phoneNumber = phoneNumber,
                            address = address,
                            onSave = { updatedFullName, updatedEmail, updatedPhoneNumber, updatedAddress ->
                                saveUserDetails(
                                    context = this@MainActivity,
                                    name = updatedFullName,
                                    emailOrPhone = updatedEmail,
                                    password = "", // Password not updated in this context
                                    role = UserType.CUSTOMER
                                )
                                Toast.makeText(
                                    this@MainActivity,
                                    "Thông tin người mua hàng đã được cập nhật thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("HomeScreen") // Navigate back to the home screen
                            }
                        )
                    }

                    composable(
                        "ProfileSellerScreen/{username}/{fullName}/{address}/{openingHours}/{taxCode}/{email}/{phoneNumber}",
                        arguments = listOf(
                            navArgument("username") { type = NavType.StringType },
                            navArgument("fullName") { type = NavType.StringType },
                            navArgument("address") { type = NavType.StringType },
                            navArgument("openingHours") { type = NavType.StringType },
                            navArgument("taxCode") { type = NavType.StringType },
                            navArgument("email") { type = NavType.StringType },
                            navArgument("phoneNumber") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        val fullName = backStackEntry.arguments?.getString("fullName") ?: ""
                        val address = backStackEntry.arguments?.getString("address") ?: ""
                        val openingHours = backStackEntry.arguments?.getString("openingHours") ?: ""
                        val taxCode = backStackEntry.arguments?.getString("taxCode") ?: ""
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""

                        ProfileSellerScreen(
                            username = username,
                            fullName = fullName,
                            address = address,
                            openingHours = openingHours,
                            taxCode = taxCode,
                            email = email,
                            phoneNumber = phoneNumber,
                            onSave = { updatedFullName, updatedAddress, updatedOpeningHours, updatedTaxCode, updatedEmail, updatedPhoneNumber ->
                                // Save the updated details
                                saveUserDetails(
                                    context = this@MainActivity,
                                    name = updatedFullName,
                                    emailOrPhone = updatedEmail,
                                    password = "", // Password handling is not required in this context
                                    role = UserType.RESTAURANT_OWNER
                                )

                                Toast.makeText(
                                    this@MainActivity,
                                    "Thông tin người bán hàng đã được cập nhật thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Navigate back to the home screen or other relevant screen
                                navController.navigate("HomeScreen")
                            }
                        )
                    }

                    composable("HomeScreen") {
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
                    composable("UserProfile") {
                        UserProfile(navController, sharedPreferences)
                    }
                    composable("store_home") { StoreHomeScreen(navController,storeviewModel) }
                    composable("customer_reviews") { ReviewScreen(navController,reviewviewModel) }
                    composable("menu") { MenuSeller(navController,MenuSellerviewModel) }
                    composable("orders") { OrderSellerScreen(navController,OrderSellerviewModel) }
                    composable("revenue") { AvenueSellerScreen(navController,AvenueSellerviewModel) }
                    composable(  "addDish") { AddDishScreen(navController,MenuSellerviewModel)}
                    composable("editDish/{dishName}") { backStackEntry ->
                        val dishName = backStackEntry.arguments?.getString("dishName") ?: ""
                        val dish = MenuSellerviewModel.dishes.collectAsState().value.find { it.name == dishName }
                        if (dish != null) {
                            com.example.yummy.EditDishScreen(
                                navController = navController,
                                dish = dish,
                                MenuSellerviewModel
                            )
                        }
                    }
                    composable("deleteDish/{dishName}") { backStackEntry ->
                        val dishName = backStackEntry.arguments?.getString("dishName") ?: ""
                        val dish = MenuSellerviewModel.dishes.collectAsState().value.find { it.name == dishName }
                        if (dish != null) {
                            DeleteDishScreen(
                                navController = navController,
                                dish = dish,
                                viewModel = MenuSellerviewModel
                            )
                        }
                    }
                    composable("cancel_order/{orderId}") { backStackEntry ->
                        val orderId = backStackEntry.arguments?.getString("orderId")?.toIntOrNull()
                        if (orderId != null) {
                            CancelOrderScreen(
                                navController = navController,
                                orderId = orderId,
                                onCancelOrder = { id, reason ->
                                    OrderSellerviewModel.rejectOrderWithReason(id, reason)
                                }
                            )
                        }
                    }
                }
            }
        }
    }


    private fun saveUserDetails(
        context: Context,
        name: String,
        emailOrPhone: String,
        password: String,
        role: UserType
    ) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("emailOrPhone", emailOrPhone)
        editor.putString("password", password)
        editor.putString("role", role.name) // Save the role as its name
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
