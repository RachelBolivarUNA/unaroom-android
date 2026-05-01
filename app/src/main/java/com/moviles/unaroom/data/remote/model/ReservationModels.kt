package com.moviles.unaroom.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Reservation API models.
 *
 * On this backend, **every resource id** (user, classroom, reservation, etc.) is a **UUID / Guid**
 * serialized in JSON as a **string**.
 */

/** Request body for `POST /api/reservations` (matches typical ASP.NET Core JSON). */
data class ReservationRequest(
    val userId: String,
    val classroomId: String,
    val date: String,
    val startTime: String,
    val endTime: String
)

/** Nested classroom on reservation responses (typical EF navigation property). */
data class ReservationClassroomDto(
    @SerializedName(value = "name", alternate = ["Name", "title", "Title"])
    val name: String? = null
)

/**
 * Response item for `GET /api/reservations/user/{userId}`.
 * Supports camelCase / PascalCase and flat name or nested `classroom` / `Classroom`.
 */
data class ReservationDto(
    @SerializedName(value = "id", alternate = ["Id"])
    val id: String,
    @SerializedName(value = "classroomId", alternate = ["ClassroomId"])
    val classroomId: String,
    @SerializedName(
        value = "classroomName",
        alternate = ["ClassroomName", "classRoomName", "ClassRoomName", "roomName", "RoomName"]
    )
    val classroomName: String? = null,
    @SerializedName(value = "classroom", alternate = ["Classroom"])
    val classroom: ReservationClassroomDto? = null,
    @SerializedName(value = "date", alternate = ["Date"])
    val date: String,
    @SerializedName(value = "startTime", alternate = ["StartTime"])
    val startTime: String,
    @SerializedName(value = "endTime", alternate = ["EndTime"])
    val endTime: String
)
