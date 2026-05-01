package com.moviles.unaroom.data

/**
 * A reservation shown on the home carousel (after mapping from the API).
 */
data class UpcomingReservation(
    val id: String,
    val classroomId: String,
    val classroomDisplayName: String,
    val date: String,
    val startTime: String,
    val endTime: String
)
