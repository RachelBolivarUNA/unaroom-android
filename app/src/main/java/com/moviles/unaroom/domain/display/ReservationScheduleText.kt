package com.moviles.unaroom.domain.display

import com.moviles.unaroom.data.UpcomingReservation

/**
 * Presentation formatting for reservation schedule lines (no I/O, no Android APIs).
 */
object ReservationScheduleText {

    fun formatScheduleLine(reservation: UpcomingReservation): String {
        val datePart = reservation.date.trim()
        val start = compactTime(reservation.startTime)
        val end = compactTime(reservation.endTime)
        return "$datePart · $start – $end"
    }

    private fun compactTime(raw: String): String {
        val s = raw.trim()
        return when {
            s.length >= 8 && s[5] == ':' && s[2] == ':' -> s.substring(0, 5)
            s.length == 5 && s[2] == ':' -> s
            else -> s
        }
    }
}
