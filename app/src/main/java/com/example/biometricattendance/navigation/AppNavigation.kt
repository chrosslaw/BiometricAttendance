package com.example.biometricattendance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.biometricattendance.ui.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController, startDestination = "signup") {
        composable("signup") { SignupScreen(navController) }
        composable("password") { PasswordSetupScreen(navController, context) }
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("attendance/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
            AttendanceScreen(navController, context, userId)
        }

    }
}