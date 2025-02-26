package com.example.biometricattendance.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.biometricattendance.auth.authenticateWithBiometrics
import com.example.biometricattendance.data.AppDatabase
import com.example.biometricattendance.data.saveAttendance
import com.example.biometricattendance.data.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.biometricattendance.auth.validateLocation

@Composable
fun HomeScreen(navController: NavController, email: String, activity: AppCompatActivity) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf<User?>(null) }

    // Create the permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        locationPermissionGranted = isGranted
    }

    // Check for location permission initially
    LaunchedEffect(Unit) {
        locationPermissionGranted = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Load user data when email changes
    LaunchedEffect(email) {
        val userDao = AppDatabase.getDatabase(context).userDao()
        user = withContext(Dispatchers.IO) { userDao.getUserByEmail(email) }
    }
    // Button click logic.
    fun onCheckInOutButtonClicked(button: String, userId: Int) {
        // Check System Biometrics first.
        authenticateWithBiometrics(
            activity,
            onSuccess = {
                // If Biometrics is confirmed, check the location.
                if (locationPermissionGranted) {
                    validateLocation(context, fusedLocationClient, onSuccess = {
                        // If Location is confirmed, You can check in and out.
                        if (button == "checkin") {
                            saveAttendance(userId, "checkin", context)
                            Toast.makeText(context, "Check-in Successful!", Toast.LENGTH_SHORT).show()
                        } else if (button == "checkout") {
                            saveAttendance(userId, "checkout", context)
                            Toast.makeText(context, "Check-out Successful!", Toast.LENGTH_SHORT).show()
                        }
                    }, onFailure = {
                        Toast.makeText(context, "You are not in the office.", Toast.LENGTH_SHORT).show()
                    })
                } else {
                    Toast.makeText(context, "Location permission not granted.", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = {
                Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Wrap the UI in a Box so we can overlay the permission button at the bottom
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (user == null) {
                Text("Loading user data...")
            } else {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Side-by-side checkin/checkout buttons
                    Button(
                        onClick = { onCheckInOutButtonClicked("checkin", user!!.id) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text("Check In")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onCheckInOutButtonClicked("checkout", user!!.id) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text("Check Out")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // attendance screen button / pass in user email.
                Button(
                    onClick = { navController.navigate("attendance/$email") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    Text("View Attendance")
                }
            }
        }

        // Only show the location permission button if permission isn't granted.
        if (!locationPermissionGranted) {
            Button(
                onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(48.dp)
            ) {
                Text("Change Location Permission")
            }
        }
    }
}