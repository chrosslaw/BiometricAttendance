package com.example.biometricattendance.auth

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun validateLocation(context: Context, onSuccess: () -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            val officeLatitude = 0.0
            val officeLongitude = 0.0

            val distance = FloatArray(1)
            Location.distanceBetween(
                location.latitude, location.longitude,
                officeLatitude, officeLongitude, distance
            )

            if (distance[0] < 50) {
                onSuccess()
            } else {
                Toast.makeText(context, "You are not in the office!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}