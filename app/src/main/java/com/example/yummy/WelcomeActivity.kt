package com.example.yummy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yummy.ui.theme.YummyTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background Image with blur effect
        Image(
            painter = painterResource(id = R.drawable.welcome_screen), // Thay bằng drawable của bạn
            contentDescription = "Hình nền chào mừng",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.6f), // Làm mờ hình nền
            contentScale = ContentScale.Crop
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Chào mừng\ntới YUMMY",
                    fontSize = 50.sp,
                    color = Color.White,
                    lineHeight = 60.sp // Adjust spacing between lines
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // Increase space between sections

            // Updated description text centered
            Text(
                text = "Đặt món ăn yêu thích và giao tận nơi!",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Center alignment
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Đăng ký tài khoản",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .clickable { onSignUpClick() }
                    .padding(8.dp)
                    .background(Color.Gray.copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Đã có tài khoản? Đăng nhập",
                fontSize = 20.sp,
                color = Color.Yellow,
                modifier = Modifier.clickable { onSignInClick() }
            )
        }
    }
}
