package com.example.yummy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileBuyerScreen(
    name: String,
    contactInfo: String,
    onSave: (String, String) -> Unit
) {
    var updatedName by remember { mutableStateOf(name) }
    var updatedAddress by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Profile Buyer", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedName,
            onValueChange = { updatedName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contactInfo,
            onValueChange = {},
            label = { Text("Contact Info") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Disable editing for contact info
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedAddress,
            onValueChange = { updatedAddress = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSave(updatedName, updatedAddress)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
