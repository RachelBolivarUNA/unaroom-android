package com.moviles.unaroom.data.repository

import com.google.gson.JsonParseException
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.data.remote.ApiService

class ClassroomRepository(
    private val apiService: ApiService
) {
    suspend fun getClassrooms(): ApiResult<List<Classroom>> {
        return try {
            val response = apiService.getClassrooms()
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
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
