package com.moviles.unaroom

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.messaging.FirebaseMessaging
import com.moviles.unaroom.core.AppConstants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(AppConstants.FCM_LOG_TAG, "Device FCM token: ${task.result}")
                } else {
                    Log.e(AppConstants.FCM_LOG_TAG, "FCM token fetch failed", task.exception)
                }
            }
        enableEdgeToEdge()
        setContent {
            UnaRoomApp()
        }
    }

}
