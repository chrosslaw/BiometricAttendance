package com.example.biometricattendance.data

import androidx.room.*

@Dao
interface AttendanceDao {
    @Insert
    suspend fun insertAttendance(attendance: Attendance)

    @Update
    suspend fun updateAttendance(attendance: Attendance) // Allow updates

    @Query("SELECT * FROM attendance WHERE userId = :userId ORDER BY date DESC")
    suspend fun getAttendanceRecords(userId: Int): List<Attendance>

    @Query("SELECT * FROM attendance WHERE userId = :userId AND date = :date LIMIT 1")
    suspend fun getTodaysAttendance(userId: Int, date: String): Attendance?
}
