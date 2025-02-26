package com.example.biometricattendance.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Logic to insert and update Attendance records, i.e., Checkin and Checkout
fun saveAttendance(userId: Int, type: String, context: Context) {
    val db = AppDatabase.getDatabase(context).attendanceDao()

    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    // insert today's attendance record
    CoroutineScope(Dispatchers.IO).launch {
        val record = db.getTodaysAttendance(userId, currentDate)
        // create a record with only checkin time with today's datetime
        if (type == "checkin" && record == null) {
            val newAttendance = Attendance(
                userId = userId,
                date = currentDate,
                checkInTime = currentTime,
                checkOutTime = null
            )
            db.insertAttendance(newAttendance)
        }
        // update today's record with today's datetime
        else if (type == "checkout" && record?.checkOutTime ==  null) {
                val updatedAttendance = record?.copy(
                    checkInTime = record.checkInTime,
                    checkOutTime = currentTime
                )
                if (updatedAttendance != null) {
                    db.updateAttendance(updatedAttendance)
                }
            }
        // inform the user their record has been updated
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("Attendance", "$type recorded at $currentTime")
            Toast.makeText(context, "$type Successful!", Toast.LENGTH_SHORT).show()
        }
    }
}
