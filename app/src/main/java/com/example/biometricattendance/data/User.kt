package com.example.biometricattendance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// User Class
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String = "",
    val password: String = "",
    val biometricRegistered: Boolean = false
)