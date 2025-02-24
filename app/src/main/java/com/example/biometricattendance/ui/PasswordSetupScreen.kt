package com.example.biometricattendance.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.biometricattendance.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.biometricattendance.data.AppDatabase


@Composable
fun PasswordSetupScreen(navController: NavController, context: Context, name: String, email: String) {
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Enter Password") })
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (password.isBlank()) {
                errorMessage = "Password cannot be empty"
                return@Button
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context).userDao()
                    db.insertUser(User(id = 0, name = name, email = email, password = password))

                    withContext(Dispatchers.Main) {
                        navController.navigate("login")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        errorMessage = "Error: ${e.localizedMessage}"
                    }
                }
            }
        }) {
            Text("Sign Up")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}