package com.moviles.unaroom.domain.datetime

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Pure date/time rules for reservations (API format, UTC calendar days, picker defaults).
 * Used by ViewModels and repositories — not by Composables.
 */
object ReservationDateTime {

    private val isoDateUtc: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    private val dateTimeUtc: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    fun millisToIsoUtcDate(millis: Long): String =
        isoDateUtc.format(Date(millis))

    fun isoDateToMillisUtc(iso: String): Long? {
        if (iso.isBlank()) return null
        return try {
            isoDateUtc.parse(iso.trim())?.time
        } catch (_: Exception) {
            null
        }
    }

    fun todayUtcStartMillis(): Long {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun initialHourMinute(time: String): Pair<Int, Int> {
        val parts = time.trim().split(":")
        val h = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 9
        val m = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
        return Pair(h, m)
    }

    fun formatHourMinuteToApi(hour: Int, minute: Int): String =
        String.format(Locale.US, "%02d:%02d:00", hour, minute)

    fun normalizeToApiTime(value: String): String {
        val s = value.trim()
        val three = Regex("""^(\d{1,2}):(\d{2}):(\d{2})$""")
        val two = Regex("""^(\d{1,2}):(\d{2})$""")
        three.matchEntire(s)?.let { m ->
            val g = m.groupValues
            return "${g[1].padStart(2, '0')}:${g[2]}:${g[3]}"
        }
        two.matchEntire(s)?.let { m ->
            val g = m.groupValues
            return "${g[1].padStart(2, '0')}:${g[2]}:00"
        }
        return s
    }

    fun startMillisUtc(date: String, startTime: String): Long? =
        parseCombinedMillisUtc(date, startTime)

    fun endMillisUtc(date: String, endTime: String): Long? =
        parseCombinedMillisUtc(date, endTime)

    private fun parseCombinedMillisUtc(date: String, time: String): Long? {
        val d = date.trim()
        val t = normalizeTimeForParsing(time.trim())
        if (d.isBlank() || t.isBlank()) return null
        return try {
            dateTimeUtc.parse("$d $t")?.time
        } catch (_: Exception) {
            null
        }
    }

    private fun normalizeTimeForParsing(value: String): String {
        val three = Regex("""^(\d{1,2}):(\d{2}):(\d{2})$""")
        val two = Regex("""^(\d{1,2}):(\d{2})$""")
        three.matchEntire(value)?.let { m ->
            val g = m.groupValues
            return "${g[1].padStart(2, '0')}:${g[2]}:${g[3]}"
        }
        two.matchEntire(value)?.let { m ->
            val g = m.groupValues
            return "${g[1].padStart(2, '0')}:${g[2]}:00"
        }
        return value
    }
}
