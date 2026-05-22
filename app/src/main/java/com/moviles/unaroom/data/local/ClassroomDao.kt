package com.moviles.unaroom.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for classrooms.
 * Room generates the SQL at compile time from these annotations.
 * [getAllClassrooms] returns a [Flow] so the UI reacts automatically to table changes.
 */
@Dao
interface ClassroomDao {

    @Query("SELECT * FROM classrooms ORDER BY name ASC")
    fun getAllClassrooms(): Flow<List<ClassroomEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(classrooms: List<ClassroomEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(classrooms: List<ClassroomEntity>)

    @Query("SELECT COUNT(*) FROM classrooms")
    suspend fun count(): Int
}
