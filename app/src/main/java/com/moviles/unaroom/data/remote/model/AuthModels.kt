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

/**
 * Body sent to POST /api/device-tokens/{userId}.
 * The userId is passed as a path parameter; only the token goes in the request body.
 */
data class FcmTokenRequest(
    val token: String
)

