package com.example.biometricattendance

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.biometricattendance.navigation.AppNavigation
import com.example.biometricattendance.ui.Header


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(this)
        setContent {
            Header() // Global header
            AppNavigation(this) // pass in AppCompatActivity
        }
    }
}
