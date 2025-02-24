package com.example.biometricattendance.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.biometricattendance.auth.showBiometricPrompt


@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showBiometricPrompt(context as FragmentActivity, navController) }) {
            Text("Check In")
        }
        Button(onClick = { showBiometricPrompt(context as FragmentActivity, navController) }) {
            Text("Check Out")
        }
        Button(onClick = { navController.navigate("attendance") }) {
            Text("View Attendance")
        }
    }
}
