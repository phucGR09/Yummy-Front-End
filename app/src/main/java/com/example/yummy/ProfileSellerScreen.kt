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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileSellerScreen(
    name: String,
    contactInfo: String,
    onSave: (updatedName: String, updatedStoreName: String) -> Unit,
) {
    var updatedName by remember { mutableStateOf(name) }
    var storeName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Seller Profile", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedName,
            onValueChange = { updatedName = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = storeName,
            onValueChange = { storeName = it },
            label = { Text("Store Name") },
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

        Button(
            onClick = {
                if (updatedName.isEmpty() || storeName.isEmpty()) {
                    Toast.makeText(context, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                } else {
                    onSave(updatedName, storeName)
                    Toast.makeText(context, "Seller Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
