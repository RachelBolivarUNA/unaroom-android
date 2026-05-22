package com.moviles.unaroom.data.remote

import com.moviles.unaroom.core.AppConstants
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.data.remote.model.LoginRequest
import com.moviles.unaroom.data.remote.model.ReservationDto
import com.moviles.unaroom.data.remote.model.ReservationRequest
import com.moviles.unaroom.data.remote.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST

interface ApiService {
    @POST(AppConstants.Api.Paths.AUTH_LOGIN)
    suspend fun login(@Body request: LoginRequest): Response<UserDto>

    @GET(AppConstants.Api.Paths.CLASSROOMS)
    suspend fun getClassrooms(): Response<List<Classroom>>

    @GET(AppConstants.Api.Paths.RESERVATIONS_BY_USER)
    suspend fun getReservations(@Path("userId") userId: String): Response<List<ReservationDto>>

    @POST(AppConstants.Api.Paths.RESERVATIONS)
    suspend fun createReservation(@Body request: ReservationRequest): Response<Unit>
}
