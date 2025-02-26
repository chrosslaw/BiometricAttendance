package com.example.biometricattendance.data

import androidx.room.*

//DB calls for User
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Update
    suspend fun updateUser(user: User)
}
