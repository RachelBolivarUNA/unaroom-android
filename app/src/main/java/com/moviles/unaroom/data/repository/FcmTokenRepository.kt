package com.moviles.unaroom.data.repository

import android.util.Log
import com.moviles.unaroom.core.AppConstants
import com.moviles.unaroom.data.AuthSession
import com.moviles.unaroom.data.remote.ApiService
import com.moviles.unaroom.data.remote.model.FcmTokenRequest

class FcmTokenRepository(
    private val apiService: ApiService
) {

    /**
     * Sends [token] to the backend so the server can target this device.
     *
     * Does nothing if the user is not logged in yet — the token will be
     * re-registered when the user logs in
     */
    suspend fun registerToken(token: String) {
        val userId = AuthSession.currentUser?.id ?: run {
            // User hasn't logged in yet; the token will be sent after login.
            Log.d(AppConstants.FCM_LOG_TAG, "No active session — FCM token deferred until login.")
            return
        }

        try {
            // POST /api/device-tokens/{userId}  — userId in path, token in body.
            apiService.registerFcmToken(
                userId = userId,
                request = FcmTokenRequest(token = token)
            )
            Log.d(AppConstants.FCM_LOG_TAG, "FCM token registered for user $userId")
        } catch (e: Exception) {
            // Non-fatal — token registration can be retried later.
            Log.w(AppConstants.FCM_LOG_TAG, "FCM token registration failed: ${e.message}")
        }
    }
}


