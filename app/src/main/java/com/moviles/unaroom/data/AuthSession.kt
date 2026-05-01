package com.moviles.unaroom.data

import com.moviles.unaroom.data.remote.model.UserDto

/**
 * Holds the signed-in user for the current app session (simple approach for an academic MVVM app).
 * Cleared on logout. Used when creating reservations so the API receives [UserDto.id] as `userId`.
 */
object AuthSession {
    @Volatile
    var currentUser: UserDto? = null
        private set

    fun setUser(user: UserDto) {
        currentUser = user
    }

    fun clear() {
        currentUser = null
    }
}
