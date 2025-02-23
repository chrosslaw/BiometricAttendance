package com.example.biometricattendance.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.biometricattendance.ui.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "signup") {
        composable("signup") { SignupScreen(navController) }
        // *** Need to create these in ui
//        composable("password") { PasswordSetupScreen(navController) }
//        composable("login") { LoginScreen(navController) }
//        composable("home") { HomeScreen(navController) }
    }
}