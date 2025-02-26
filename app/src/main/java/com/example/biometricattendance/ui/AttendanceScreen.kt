package com.example.biometricattendance.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.biometricattendance.data.Attendance
import com.example.biometricattendance.data.User
import androidx.compose.ui.platform.LocalContext
import com.example.biometricattendance.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AttendanceScreen(
    navController: NavController,
    email: String,

) {
    // States to hold the fetched user, today's attendance, and history
    var user by remember { mutableStateOf<User?>(null) }
    var todaysAttendance by remember { mutableStateOf<Attendance?>(null) }
    var attendanceRecords by remember { mutableStateOf<List<Attendance>>(emptyList()) }
    val userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
    val attendanceDao = AppDatabase.getDatabase(LocalContext.current).attendanceDao()
    // Helper function to format current date as "yyyy-MM-dd"
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // Launch a coroutine to fetch data from the database
    LaunchedEffect(email) {
        // Fetch the user object using the email
        user = withContext(Dispatchers.IO) { userDao.getUserByEmail(email) }
        user?.let { currentUser ->
            val today = getCurrentDate()
            // Fetch today's attendance record (if any)
            todaysAttendance = withContext(Dispatchers.IO) {
                attendanceDao.getTodaysAttendance(currentUser.id, today)
            }
            // Fetch all attendance records for the user
            attendanceRecords = withContext(Dispatchers.IO) {
                attendanceDao.getAttendanceRecords(currentUser.id)
            }
        }
    }

    Scaffold(
        topBar = {
            Text("Attendance History")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            // Display today's attendance status, if available
            if (todaysAttendance != null) {
                Text(
                    text = "Today's Attendance:\n" +
                            "Check-In: ${todaysAttendance?.checkInTime ?: "Not Checked In"}\n" +
                            "Check-Out: ${todaysAttendance?.checkOutTime ?: "Not Checked Out"}",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text("No attendance record for today", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display the attendance history using a LazyColumn
            LazyColumn {
                items(attendanceRecords) { attendance ->
                    AttendanceItem(attendance = attendance)
                }
            }

        }
        Column(
                modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
        ) {
            // A button to navigate back to Home
            Button(
                onClick = { navController.navigate("home/$email") },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding( 60.dp)
            ) {
                Text("Home")
            }
        }
    }
}
