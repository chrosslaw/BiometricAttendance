package com.example.biometricattendance.ui

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SignupScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Enter Name") })
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = email, onValueChange = {
            email = it
            emailError = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Invalid email" else ""
        }, label = { Text("Enter Email") }, isError = emailError.isNotEmpty())

        if (emailError.isNotEmpty()) {
            Text(text = emailError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            if (emailError.isEmpty()) {
                navController.navigate("password")
            }
        }) {
            Text("Next")
        }
    }
}