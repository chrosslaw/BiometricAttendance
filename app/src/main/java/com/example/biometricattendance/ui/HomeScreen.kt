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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var locationPermissionGranted by remember { mutableStateOf(false) }

    fun onCheckInOutButtonClicked(button: String, userId: Int, activity: AppCompatActivity, context: Context) {
        authenticateWithBiometrics(activity,
            onSuccess = {
                if (locationPermissionGranted) {
                    validateLocation(context, fusedLocationClient, onSuccess = {
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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        locationPermissionGranted = isGranted
    }

    // Check for existing permission
    LaunchedEffect(Unit) {
        locationPermissionGranted = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(email) {
        val userDao = AppDatabase.getDatabase(context).userDao()
        user = withContext(Dispatchers.IO) { userDao.getUserByEmail(email) }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user == null) {
            Text("Loading user data...")
        } else {
            Button(onClick = {
                onCheckInOutButtonClicked("checkin", user!!.id, activity, context)
            }) {
                Text("Check In")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                onCheckInOutButtonClicked("checkout", user!!.id, activity, context)
            }) {
                Text("Check Out")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }) {
            Text("Request Location Permission")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("attendance/$email")
        }) {
            Text("View Attendance")
        }
    }
}



