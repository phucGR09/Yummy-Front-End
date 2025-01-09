package com.example.yummy

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpScreen(
    onSignUpSuccess: (String, String, String, String) -> Unit,
    onSignInClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sign Up", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            label = { Text("Email/Phone") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Role Selection with Radio Buttons
        Text("Select Your Role", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        RoleSelectionRadioButton(
            selectedRole = selectedRole,
            onRoleSelected = { selectedRole = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    name.isEmpty() || emailOrPhone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                        Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                    }
                    selectedRole.isEmpty() -> {
                        Toast.makeText(context, "Please select a role", Toast.LENGTH_SHORT).show()
                    }
                    password != confirmPassword -> {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                    !isValidPassword(password) -> {
                        Toast.makeText(
                            context,
                            "Password must be at least 8 characters, include uppercase, lowercase, digit, and special character.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    !isValidContactInfo(emailOrPhone) -> {
                        Toast.makeText(context, "Enter a valid email or phone number", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Perform sign-up logic
                        onSignUpSuccess(name, emailOrPhone, password, selectedRole)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onSignInClick) {
            Text("Already have an account? Sign in")
        }
    }
}

@Composable
fun RoleSelectionRadioButton(
    selectedRole: String,
    onRoleSelected: (String) -> Unit
) {
    val roles = listOf("Buyer", "Seller", "Shipper")

    Column {
        roles.forEach { role ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = role == selectedRole,
                    onClick = { onRoleSelected(role) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(role)
            }
        }
    }
}

fun isValidPassword(password: String): Boolean {
    val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=]).{8,}$"
    return password.matches(passwordPattern.toRegex())
}

fun isValidContactInfo(contactInfo: String): Boolean {
    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    return if (emailPattern.matcher(contactInfo).matches()) {
        true // Valid email
    } else {
        contactInfo.matches(Regex("^[0-9]{10,11}$")) // Valid phone number
    }
}
