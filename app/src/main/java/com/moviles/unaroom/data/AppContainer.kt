package com.moviles.unaroom.data

import android.content.Context
import com.moviles.unaroom.core.NetworkMonitor
import com.moviles.unaroom.data.local.UnaRoomDatabase
import com.moviles.unaroom.data.remote.RetrofitClient
import com.moviles.unaroom.data.repository.AuthRepository
import com.moviles.unaroom.data.repository.ClassroomRepository
import com.moviles.unaroom.data.repository.FcmTokenRepository
import com.moviles.unaroom.data.repository.ReservationRepository


object AppContainer {
    private val apiService = RetrofitClient.apiService
    private lateinit var database: UnaRoomDatabase

    lateinit var networkMonitor: NetworkMonitor
        private set

    fun init(context: Context) {
        database = UnaRoomDatabase.getInstance(context)
        networkMonitor = NetworkMonitor(context)
    }

    val authRepository: AuthRepository by lazy { AuthRepository(apiService) }

    val classroomRepository: ClassroomRepository by lazy {
        ClassroomRepository(apiService, database.classroomDao())
    }

    val reservationRepository: ReservationRepository by lazy {
        ReservationRepository(apiService, classroomRepository)
    }

    val fcmTokenRepository: FcmTokenRepository by lazy { FcmTokenRepository(apiService) }
}
