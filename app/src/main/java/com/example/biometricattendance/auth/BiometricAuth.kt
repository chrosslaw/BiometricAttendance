package com.example.biometricattendance.auth

import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import java.util.concurrent.Executors

// Biometric logic
fun authenticateWithBiometrics(activity: AppCompatActivity, onSuccess: () -> Unit, onFailure: () -> Unit) {
    val executor = Executors.newSingleThreadExecutor()

    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                activity.runOnUiThread { onSuccess() }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                activity.runOnUiThread { onFailure() }
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                activity.runOnUiThread {
                    Toast.makeText(activity, "Biometric error: $errString", Toast.LENGTH_SHORT).show()
                }
            }

        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Authenticate to check in/out")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(promptInfo)
}