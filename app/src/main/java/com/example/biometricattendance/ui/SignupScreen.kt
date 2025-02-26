package com.example.biometricattendance.ui

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        // Page header
        Text(text = "Welcome!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))
        // User input - name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        // User input - email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (email.isBlank()) {
                    "Email is required"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    "Invalid email"
                } else ""
            },
            label = { Text("Enter Email") },
            isError = emailError.isNotEmpty()
        )

        if (emailError.isNotEmpty()) {
            Text(text = emailError, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(20.dp))
        // Sign in and sign up buttons
        Button(
            onClick = {
                if (email.isBlank()) {
                    emailError = "Email is required"
                } else if (emailError.isEmpty()) {
                    navController.navigate("password/$name/$email")
                }
            }
        ) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("or",fontSize = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate("login") }) {
            Text("Sign In Here")
        }
    }
}
