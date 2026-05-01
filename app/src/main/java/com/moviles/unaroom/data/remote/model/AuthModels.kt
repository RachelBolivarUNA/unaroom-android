package com.moviles.unaroom.data.remote.model

data class LoginRequest(
    val email: String,
    val password: String
)

/** Logged-in user; [id] is a Guid string from the API. */
data class UserDto(
    val id: String,
    val name: String,
    val email: String
)
