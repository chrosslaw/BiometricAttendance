package com.example.biometricattendance.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.biometricattendance.data.AppDatabase
import com.example.biometricattendance.data.User


@Composable
fun PasswordSetupScreen(navController: NavController, name: String, email: String) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        // Page Header
        Text("Choose a Password.",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Enter Password") })
        OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") })
        Spacer(modifier = Modifier.height(20.dp))
        // Password create
        Button(onClick = {
            // PW can't be blank
            if (password.isBlank()) {
                errorMessage = "Password cannot be empty"
                return@Button
            }
            when {
                // passwords must match
                password.isEmpty() || confirmPassword.isEmpty() -> errorMessage = "Password fields cannot be empty"
                password != confirmPassword -> errorMessage = "Passwords do not match"
                // enter user, email, and password into db
                else ->  CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(context).userDao()
                        db.insertUser(User( name = name, email = email, password = password))

                        withContext(Dispatchers.Main) {
                            navController.navigate("login")
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            errorMessage = "Error: ${e.localizedMessage}"
                        }
                    }
                }
            }

        },modifier = Modifier.align(Alignment.End).padding(horizontal = 60.dp)) {
            Text("Sign Up")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}