package com.example.biometricattendance.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Attendance Class
@Entity(
    tableName = "attendance",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE // Deletes attendance if user is removed
    )]
)
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val date: String,
    val checkInTime: String?,
    val checkOutTime: String?
)