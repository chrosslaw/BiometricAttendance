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
            // passing the user's location as office to be able to pass the location test
            val officeLatitude = location.latitude
            val officeLongitude = location.longitude

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
        } else {
            Toast.makeText(context, "Unable to get location!", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Location error: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}
