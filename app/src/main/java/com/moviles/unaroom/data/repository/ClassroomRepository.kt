package com.moviles.unaroom.data.repository

import com.google.gson.JsonParseException
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.data.local.ClassroomDao
import com.moviles.unaroom.data.local.ClassroomEntity
import com.moviles.unaroom.data.local.toClassroom
import com.moviles.unaroom.data.local.toEntity
import com.moviles.unaroom.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Single source of truth for classroom data.
 * Room is the source of truth for the UI; the API is used to keep it up to date.
 *
 * Data flow:  API ──upsert──▶ Room ──Flow──▶ ViewModel ──StateFlow──▶ Screen
 */
class ClassroomRepository(
    private val apiService: ApiService,
    private val classroomDao: ClassroomDao
) {

    /** Reactive stream of classrooms from Room. Re-emits automatically on any table change. */
    fun getClassroomsFlow(): Flow<List<Classroom>> =
        classroomDao.getAllClassrooms().map { entities -> entities.map { it.toClassroom() } }

    /** Inserts sample data on first launch so the app shows content without a running backend. */
    suspend fun seedIfEmpty() {
        if (classroomDao.count() == 0) {
            classroomDao.insertAll(
                listOf(
                    ClassroomEntity("11111111-0000-0000-0000-000000000001", "Room A-101",  30,  "Building A, Floor 1"),
                    ClassroomEntity("11111111-0000-0000-0000-000000000002", "Room B-204",  20,  "Building B, Floor 2"),
                )
            )
        }
    }

    /**
     * Fetches classrooms from the API and upserts them into Room.
     * The [getClassroomsFlow] collector will emit the updated list automatically.
     */
    suspend fun getClassrooms(): ApiResult<List<Classroom>> {
        return try {
            val response = apiService.getClassrooms()
            if (response.isSuccessful && response.body() != null) {
                val classrooms = response.body()!!
                classroomDao.upsertAll(classrooms.map { it.toEntity() })
                ApiResult.Success(classrooms)
            } else {
                ApiResult.Error(
                    message = UserMessages.Classrooms.FETCH_FAILED,
                    statusCode = response.code()
                )
            }
        } catch (_: JsonParseException) {
            ApiResult.Error(message = UserMessages.Classrooms.JSON_MISMATCH)
        } catch (_: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }
}
