package com.example.biometricattendance.navigation

import HomeScreen
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.biometricattendance.ui.*

@Composable
fun AppNavigation(activity: AppCompatActivity) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "signup") {
        composable("signup") { SignupScreen(navController) }
        composable("password/{name}/{email}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            PasswordSetupScreen(navController, name, email)
        }
        composable("login") { LoginScreen(navController) }
        composable(
            "home/{email}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            HomeScreen(navController, email, activity)
        }
        composable("attendance/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            AttendanceScreen(navController, email)
        }
    }
}