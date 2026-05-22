package com.moviles.unaroom.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moviles.unaroom.data.Classroom

/**
 * Room entity — each instance represents one row in the "classrooms" SQLite table.
 *
 * @Entity  tells Room to create a table for this class.
 * @PrimaryKey  marks the column that uniquely identifies each row.
 *
 * Room generates the actual SQL (CREATE TABLE, INSERT, SELECT …) at compile time
 * based on these annotations.
 */
@Entity(tableName = "classrooms")
data class ClassroomEntity(
    @PrimaryKey val id: String,
    val name: String,
    val capacity: Int,
    val location: String
)

// ── Mapping helpers ──────────────────────────────────────────────────────────
// These keep Room details out of the rest of the app. The UI always works with
// the plain Classroom domain model, not with the database-specific entity.

/** Convert a database row → domain model (used by the ViewModel / UI). */
fun ClassroomEntity.toClassroom(): Classroom = Classroom(
    id = id,
    name = name,
    capacity = capacity,
    location = location
)

/** Convert a domain model → database row (used when saving to Room). */
fun Classroom.toEntity(): ClassroomEntity = ClassroomEntity(
    id = id,
    name = name,
    capacity = capacity,
    location = location
)

