package com.example.yummy

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
fun ProfileShipperScreen(
    name: String,
    contactInfo: String,
    onSave: (updatedName: String, updatedAddress: String) -> Unit
) {
    var updatedName by remember { mutableStateOf(name) }
    var address by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Launchers for uploading CCCD and License images
    val cccdLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        Toast.makeText(context, "CCCD uploaded successfully!", Toast.LENGTH_SHORT).show()
    }
    val licenseLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        Toast.makeText(context, "License uploaded successfully!", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Shipper Profile", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = updatedName,
            onValueChange = { updatedName = it },
            label = { Text("Your Name") },
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
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { cccdLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload CCCD")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { licenseLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload Driver's License")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (updatedName.isEmpty() || address.isEmpty()) {
                    Toast.makeText(context, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                } else {
                    onSave(updatedName, address)
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
