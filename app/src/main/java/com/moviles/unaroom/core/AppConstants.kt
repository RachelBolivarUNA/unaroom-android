package com.moviles.unaroom.core

/**
 * Non-UI technical constants (URLs, tags, build-time config).
 * User-visible copy lives in [UserMessages].
 */
object AppConstants {

    const val FCM_LOG_TAG = "FCM"

    object Api {
        /** Emulator reaches host machine at 10.0.2.2; change port to match your API. */
        const val BASE_URL = "http://10.0.2.2:5147/"

        object Paths {
            const val AUTH_LOGIN = "api/auth/login"
            const val CLASSROOMS = "api/classrooms"
            const val RESERVATIONS_BY_USER = "api/reservations/user/{userId}"
            const val RESERVATIONS = "api/reservations"
        }
    }
}
