import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.biometricattendance.auth.authenticateWithBiometrics
import com.example.biometricattendance.auth.validateLocation
import com.example.biometricattendance.data.AppDatabase
import com.example.biometricattendance.data.saveAttendance
import com.example.biometricattendance.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun HomeScreen(navController: NavController, email: String, activity: AppCompatActivity) {
    val context = LocalContext.current
    fun onCheckInOutButtonClicked(button: String, userId: Int,activity: AppCompatActivity, context: Context ) {
        authenticateWithBiometrics(activity,
            onSuccess = {
                // Biometric authentication successful
                // Proceed with location check and attendance record creation
                val db = AppDatabase.getDatabase(context).userDao()
                validateLocation(context, onSuccess = {
                    if (button == "checkin") {
                        saveAttendance(userId, "checkin", context)
                        Toast.makeText(context, "Check-in Successful!", Toast.LENGTH_SHORT).show()
                    } else if (button == "checkout") {
                        saveAttendance(userId, "checkout", context)
                        Toast.makeText(context, "Check-out Successful!", Toast.LENGTH_SHORT).show()
                    }
                })
            },
            onFailure = {
                // Biometric authentication failed; display an error message
                Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        )
    }
    // State to hold the current user (fetched from the database)
    var user by remember { mutableStateOf<User?>(null) }

    // Load the user data when the screen appears
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
            // If user is not biometric registered, show the dialog trigger

                // CASE 2: User is already registered. Show Check In/Out buttons.
                // Check In Button
                Button(onClick = {
                    onCheckInOutButtonClicked("checkin", user!!.id, activity, context)
                }
                ) {
                    Text("Check In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Check Out Button
                Button(onClick = {
                    onCheckInOutButtonClicked("checkout", user!!.id, activity, context)
                }
                ) {
                    Text("Check Out")
                }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // View Attendance Button (always available)
        Button(onClick = {
            navController.navigate("attendance/$email")
        }) {
            Text("View Attendance")
        }
    }
}
