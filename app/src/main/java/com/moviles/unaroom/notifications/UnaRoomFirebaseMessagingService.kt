package com.moviles.unaroom.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moviles.unaroom.core.AppConstants
import com.moviles.unaroom.data.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class UnaRoomFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token received: $token")

        // Network calls must not run on the main thread, so we use a coroutine.
        // SupervisorJob prevents one failure from cancelling other coroutines.
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            AppContainer.fcmTokenRepository.registerToken(token)
        }
    }

    /**
     * Called when a push message arrives while the app is in the foreground.
     *
     * For background messages that contain a `notification` payload, Android
     * displays them automatically without calling this method.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "UnaRoom"
        val body  = message.notification?.body  ?: "You have a new notification"

        Log.d(TAG, "Message received — title: $title")

        // Delegate to NotificationHelper to keep this method small and readable.
        NotificationHelper.show(context = this, title = title, body = body)
    }

    companion object {
        private const val TAG = AppConstants.FCM_LOG_TAG
    }
}
