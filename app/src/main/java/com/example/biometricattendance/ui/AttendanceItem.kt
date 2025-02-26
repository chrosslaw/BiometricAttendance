package com.example.biometricattendance.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.biometricattendance.data.Attendance


@Composable
fun AttendanceItem(attendance: Attendance) {
    // UI for each daily entry
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Date: ${attendance.date}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Check-in: ${attendance.checkInTime ?: "Not Checked In"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Check-out: ${attendance.checkOutTime ?: "Not Checked Out"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}