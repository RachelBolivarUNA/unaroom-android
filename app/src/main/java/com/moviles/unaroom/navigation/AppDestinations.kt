package com.moviles.unaroom.navigation

import android.net.Uri

object AppDestinations {
    const val LOGIN = "login"
    const val HOME = "home"
    const val CLASSROOMS = "classrooms"
    const val CLASSROOM_DETAIL = "classroomDetail"
    const val RESERVATION_CREATE = "reservationCreate"
    const val CALENDAR = "calendar"
    const val PROFILE = "profile"

    fun classroomDetailRoute(id: String, name: String, capacity: Int, location: String): String {
        return "${CLASSROOM_DETAIL}/${Uri.encode(id)}/${Uri.encode(name)}/$capacity/${Uri.encode(location)}"
    }

    /** Pass [classroomId] / [classroomName] when opening from a room; both empty to pick a room on the form. */
    fun reservationCreateRoute(
        classroomId: String? = null,
        classroomName: String? = null
    ): String {
        val encId = if (classroomId.isNullOrBlank()) "" else Uri.encode(classroomId)
        val encName = if (classroomName.isNullOrBlank()) "" else Uri.encode(classroomName)
        return "$RESERVATION_CREATE?classroomId=$encId&classroomName=$encName"
    }
}
