package com.moviles.unaroom.data.repository

import com.google.gson.JsonParseException
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.UpcomingReservation
import com.moviles.unaroom.data.remote.ApiService
import com.moviles.unaroom.data.remote.model.ReservationDto
import com.moviles.unaroom.data.remote.model.ReservationRequest
import com.moviles.unaroom.domain.datetime.ReservationDateTime

class ReservationRepository(
    private val apiService: ApiService,
    private val classroomRepository: ClassroomRepository
) {
    suspend fun createReservation(
        userId: String,
        classroomId: String,
        date: String,
        startTime: String,
        endTime: String
    ): ApiResult<Unit> {
        return try {
            val request = ReservationRequest(
                userId = userId,
                classroomId = classroomId,
                date = date.trim(),
                startTime = startTime.trim(),
                endTime = endTime.trim()
            )
            val response = apiService.createReservation(request)
            if (response.isSuccessful) {
                ApiResult.Success(Unit)
            } else {
                val message = if (response.code() == 409) {
                    UserMessages.ReservationApi.CONFLICT
                } else {
                    UserMessages.ReservationApi.CREATE_FAILED
                }
                ApiResult.Error(message = message, statusCode = response.code())
            }
        } catch (_: JsonParseException) {
            ApiResult.Error(message = UserMessages.ReservationApi.CREATE_JSON_MISMATCH)
        } catch (_: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }

    /**
     * Loads reservations for the user and returns only future (or in-progress) items, soonest first.
     * Expects `GET /api/reservations/user/{userId}` returning JSON array of [ReservationDto].
     */
    suspend fun getUpcomingReservations(userId: String): ApiResult<List<UpcomingReservation>> {
        return try {
            val response = apiService.getReservations(userId = userId.trim())
            if (response.isSuccessful) {
                val body = response.body().orEmpty()
                val now = System.currentTimeMillis()
                val upcoming = body
                    .map { it.toUpcoming() }
                    .mapNotNull { item ->
                        val end = ReservationDateTime.endMillisUtc(
                            date = item.date,
                            endTime = item.endTime
                        )
                        val start = ReservationDateTime.startMillisUtc(
                            date = item.date,
                            startTime = item.startTime
                        )
                        val stillRelevant = when {
                            end != null -> end >= now
                            start != null -> start >= now
                            else -> false
                        }
                        if (stillRelevant) item else null
                    }
                    .sortedBy { item ->
                        ReservationDateTime.startMillisUtc(
                            date = item.date,
                            startTime = item.startTime
                        ) ?: Long.MAX_VALUE
                    }
                ApiResult.Success(enrichClassroomDisplayNames(upcoming))
            } else {
                ApiResult.Error(
                    message = UserMessages.ReservationApi.LIST_FAILED,
                    statusCode = response.code()
                )
            }
        } catch (_: JsonParseException) {
            ApiResult.Error(message = UserMessages.ReservationApi.LIST_JSON_MISMATCH)
        } catch (_: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }

    private fun ReservationDto.toUpcoming(): UpcomingReservation {
        val name = resolveClassroomDisplayName()
        return UpcomingReservation(
            id = id.trim(),
            classroomId = classroomId.trim(),
            classroomDisplayName = name,
            date = date.trim(),
            startTime = startTime.trim(),
            endTime = endTime.trim()
        )
    }

    private fun ReservationDto.resolveClassroomDisplayName(): String {
        val flat = classroomName?.trim().orEmpty()
        if (flat.isNotEmpty()) return flat
        val nested = classroom?.name?.trim().orEmpty()
        if (nested.isNotEmpty()) return nested
        return ""
    }

    /**
     * When the reservations API omits the room label, resolve it from `GET /api/classrooms`.
     */
    private suspend fun enrichClassroomDisplayNames(
        items: List<UpcomingReservation>
    ): List<UpcomingReservation> {
        if (items.none { it.classroomDisplayName.isBlank() }) {
            return items.map { it.withFallbackLabel() }
        }
        val byId = when (val result = classroomRepository.getClassrooms()) {
            is ApiResult.Success -> result.data.associateBy { it.id.trim() }
            is ApiResult.Error -> emptyMap()
        }
        return items.map { item ->
            val label = item.classroomDisplayName.trim()
            if (label.isNotEmpty()) {
                item.withFallbackLabel()
            } else {
                val fromApi = byId[item.classroomId.trim()]?.name?.trim().orEmpty()
                item.copy(
                    classroomDisplayName = fromApi.ifEmpty { UserMessages.GENERIC_CLASSROOM_LABEL }
                )
            }
        }
    }

    private fun UpcomingReservation.withFallbackLabel(): UpcomingReservation {
        val label = classroomDisplayName.trim()
        return if (label.isNotEmpty()) {
            this
        } else {
            copy(classroomDisplayName = UserMessages.GENERIC_CLASSROOM_LABEL)
        }
    }
}
