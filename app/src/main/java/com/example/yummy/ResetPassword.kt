package com.example.yummy

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPassword(
    navController: NavController,
    sharedPreferences: SharedPreferences
) {
    var email by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reset Password") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Input email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email or Phone") },
                placeholder = { Text("Enter your email or phone") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                isError = showError,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    disabledBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            if (showError) {
                Text(
                    text = "Please enter a valid email or phone.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Send OTP Button
            Button(
                onClick = {
                    if (email.trim().isEmpty()) {
                        showError = true
                    } else {
                        showError = false
                        if (isEmailRegistered(email, sharedPreferences)) {
                            navController.navigate("VerificationCode/$email/true")
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Email/Phone not found.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send OTP")
            }
        }
    }
}

// Function to check if email is registered
fun isEmailRegistered(email: String, sharedPreferences: SharedPreferences): Boolean {
    val registeredEmail = sharedPreferences.getString("email", null)
    return email == registeredEmail
}

