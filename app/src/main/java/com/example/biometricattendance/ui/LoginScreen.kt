package com.example.biometricattendance.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.biometricattendance.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Email and Password cannot be empty"
                    return@Button
                }

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(context).userDao()
                        val user = db.getUserByEmail(email)

                        withContext(Dispatchers.Main) {
                            if (user != null && user.password == password) {
                                navController.navigate("home/$email")
                            } else {
                                errorMessage = "Invalid email or password"
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage = "Error: ${e.localizedMessage}"
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.End).padding(horizontal = 60.dp)
        ) {
            Text("Login")
        }
    }
}
