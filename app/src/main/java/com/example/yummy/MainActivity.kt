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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




//class MainActivity : ComponentActivity() {
//    private val apiService = ApiClient.instance.create(ApiService::class.java)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Tạo dữ liệu yêu cầu
//        val createMenuItemRequest = CreateMenuItemRequest(
//            name = "Pasta Carebonarae",
//            price = 12.99,
//            description = "Classic Italian pasta with creamy sauce.",
//            imageUrl = "https://example.com/pasta-carbonara.jpg",
//            restaurantId = 1
//        )
//
//        // Gửi yêu cầu đến backend
//        apiService.createMenuItem(createMenuItemRequest).enqueue(object : Callback<CreateMenuItemResponse> {
//            override fun onResponse(call: Call<CreateMenuItemResponse>, response: Response<CreateMenuItemResponse>) {
//                if (response.isSuccessful) {
//                    val menuItem = response.body()?.result
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Created: ${menuItem?.name} (ID: ${menuItem?.id})",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Failed to create menu item: ${response.code()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            override fun onFailure(call: Call<CreateMenuItemResponse>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}


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
                            onSignUpSuccess = { fullName, username, email, phone, password, role ->
                                val generatedOtp = generateOtp()
                                navController.navigate("verification_code_screen/$fullName/$username/$email/$phone/$password/$role/$generatedOtp")
                            },
                            onSignInClick = { navController.navigate("sign_in_screen") }
                        )
                    }

                    composable("sign_in_screen") {
                        SignInScreen(
                            sharedPreferences = sharedPreferences, // Pass SharedPreferences here
                            onSignInSuccess = {
                                Toast.makeText(this@MainActivity, "Sign-in successful!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home_screen")
                            },
                            onSignUpClick = { navController.navigate("sign_up_screen") }
                        )
                    }
                    composable(
                        "verification_code_screen/{fullName}/{username}/{email}/{phone}/{password}/{role}/{generatedOtp}",
                        arguments = listOf(
                            navArgument("fullName") { type = NavType.StringType },
                            navArgument("username") { type = NavType.StringType },
                            navArgument("email") { type = NavType.StringType },
                            navArgument("phone") { type = NavType.StringType },
                            navArgument("password") { type = NavType.StringType },
                            navArgument("role") { type = NavType.StringType },
                            navArgument("generatedOtp") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val fullName = backStackEntry.arguments?.getString("fullName") ?: ""
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        val phone = backStackEntry.arguments?.getString("phone") ?: ""
                        val password = backStackEntry.arguments?.getString("password") ?: ""
                        val role = backStackEntry.arguments?.getString("role") ?: ""
                        val generatedOtp = backStackEntry.arguments?.getString("generatedOtp") ?: ""

                        // Combine extracted parameters into a SignUpData object
                        val signUpData = SignUpData(
                            fullName = fullName,
                            username = username,
                            email = email,
                            phone = phone,
                            password = password,
                            role = UserType.valueOf(role.uppercase())
                        )

                        VerificationCodeScreen(
                            signUpData = signUpData,
                            generatedOtp = generatedOtp,
                            onVerifySuccess = { verifiedData ->
                                saveUserDetails(
                                    context = this@MainActivity,
                                    name = verifiedData.fullName,
                                    emailOrPhone = verifiedData.email,
                                    password = verifiedData.password,
                                    role = verifiedData.role
                                )
                                Toast.makeText(
                                    this@MainActivity,
                                    "Xác minh thành công! Thông tin người dùng đã được lưu.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Navigate to the respective profile screen
                                when (verifiedData.role) {
                                    UserType.CUSTOMER -> navController.navigate("profile_buyer_screen/${verifiedData.username}/${verifiedData.fullName}/${verifiedData.email}/${verifiedData.phone}/AddressPlaceholder")
                                    UserType.RESTAURANT_OWNER -> navController.navigate("profile_seller_screen/${verifiedData.username}/${verifiedData.fullName}/AddressPlaceholder/8:00-18:00/123456/${verifiedData.email}/${verifiedData.phone}")
                                    UserType.DELIVERY_DRIVER -> navController.navigate("profile_shipper_screen/${verifiedData.fullName}/${verifiedData.username}/${verifiedData.email}/${verifiedData.phone}/CCCDPlaceholder/LicensePlaceholder/AvatarPlaceholder")
                                }
                            },
                            onResendOtp = {
                                val newOtp = generateOtp()
                                Toast.makeText(
                                    this@MainActivity,
                                    "Mã OTP mới đã được gửi tới số điện thoại $phone. Mã OTP: $newOtp",
                                    Toast.LENGTH_SHORT
                                ).show()
                                newOtp
                            }
                        )
                    }








                    composable(
                        "profile_shipper_screen/{fullName}/{username}/{email}/{phoneNumber}/{cccd}/{license}/{avatarUrl}",
                        arguments = listOf(
                            navArgument("fullName") { type = NavType.StringType },
                            navArgument("username") { type = NavType.StringType },
                            navArgument("email") { type = NavType.StringType },
                            navArgument("phoneNumber") { type = NavType.StringType },
                            navArgument("cccd") { type = NavType.StringType },
                            navArgument("license") { type = NavType.StringType },
                            navArgument("avatarUrl") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val fullName = backStackEntry.arguments?.getString("fullName") ?: ""
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
                        val cccd = backStackEntry.arguments?.getString("cccd") ?: ""
                        val license = backStackEntry.arguments?.getString("license") ?: ""
                        val avatarUrl = backStackEntry.arguments?.getString("avatarUrl") ?: ""

                        ProfileShipperScreen(
                            fullName = fullName,
                            username = username,
                            email = email,
                            phoneNumber = phoneNumber,
                            cccd = cccd,
                            license = license,
                            avatarUrl = avatarUrl,
                            onSave = { updatedFullName, updatedUsername, updatedEmail, updatedPhoneNumber, updatedCCCD, updatedLicense, updatedAvatarUrl ->
                                // Save the updated details
                                saveUserDetails(
                                    context = this@MainActivity,
                                    name = updatedFullName,
                                    emailOrPhone = updatedEmail,
                                    password = "", // Password handling not required here
                                    role = UserType.DELIVERY_DRIVER
                                )

                                Toast.makeText(
                                    this@MainActivity,
                                    "Thông tin đã được cập nhật thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Navigate back to home screen or perform other actions
                                navController.navigate("home_screen")
                            }
                        )
                    }

                    composable(
                        "profile_buyer_screen/{username}/{fullName}/{email}/{phoneNumber}/{address}",
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
                                navController.navigate("home_screen") // Navigate back to the home screen
                            }
                        )
                    }

                    composable(
                        "profile_seller_screen/{username}/{fullName}/{address}/{openingHours}/{taxCode}/{email}/{phoneNumber}",
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
