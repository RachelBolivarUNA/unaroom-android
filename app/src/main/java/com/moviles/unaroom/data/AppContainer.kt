package com.moviles.unaroom.data

import com.moviles.unaroom.data.remote.RetrofitClient
import com.moviles.unaroom.data.repository.AuthRepository
import com.moviles.unaroom.data.repository.ClassroomRepository
import com.moviles.unaroom.data.repository.ReservationRepository

object AppContainer {
    private val apiService = RetrofitClient.apiService

    val authRepository: AuthRepository by lazy { AuthRepository(apiService) }
    val classroomRepository: ClassroomRepository by lazy { ClassroomRepository(apiService) }
    val reservationRepository: ReservationRepository by lazy {
        ReservationRepository(apiService, classroomRepository)
    }
}
