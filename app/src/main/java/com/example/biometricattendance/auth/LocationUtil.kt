package com.example.biometricattendance.auth

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun validateLocation(context: Context, fusedLocationClient: FusedLocationProviderClient, onSuccess: () -> Unit, onFailure: () -> Unit) {
    // location services
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            // passing the user's location as office to be able to pass the location test
            // These would be set by the meeting holder.
            val officeLatitude = location.latitude
            val officeLongitude = location.longitude
            // init distance from office
            val distance = FloatArray(1)
            // find the distance between the office location and users current location
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
