package com.moviles.unaroom.data.repository

import com.google.gson.JsonParseException
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.AuthSession
import com.moviles.unaroom.data.remote.ApiService
import com.moviles.unaroom.data.remote.model.LoginRequest
import com.moviles.unaroom.data.remote.model.UserDto

class AuthRepository(
    private val apiService: ApiService
) {
    fun clearLocalSession() {
        AuthSession.clear()
    }

    suspend fun login(email: String, password: String): ApiResult<UserDto> {
        return try {
            val response = apiService.login(LoginRequest(email = email, password = password))
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                val message = if (response.code() == 401) {
                    UserMessages.Auth.INVALID_CREDENTIALS
                } else {
                    UserMessages.Auth.LOGIN_FAILED
                }
                ApiResult.Error(message = message, statusCode = response.code())
            }
        } catch (_: JsonParseException) {
            ApiResult.Error(message = UserMessages.Auth.LOGIN_JSON_MISMATCH)
        } catch (_: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }
}
