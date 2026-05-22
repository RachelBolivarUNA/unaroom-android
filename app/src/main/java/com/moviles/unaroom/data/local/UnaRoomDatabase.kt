package com.moviles.unaroom.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The single Room database for UnaRoom.
 *
 * @Database  lists every @Entity table and the schema version.
 *   - If you add a new entity or change a column, increment [version] and provide a Migration.
 *   - exportSchema = false  skips generating a schema JSON (fine for class projects).
 *
 * This class is abstract — Room generates the concrete implementation at compile time.
 */
@Database(
    entities = [ClassroomEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UnaRoomDatabase : RoomDatabase() {

    abstract fun classroomDao(): ClassroomDao

    companion object {
        @Volatile
        private var INSTANCE: UnaRoomDatabase? = null

        /**
         * Returns the singleton database instance.
         * Creates it once (thread-safely with synchronized) and reuses it afterwards.
         *
         * Always use this method — never instantiate [UnaRoomDatabase] directly.
         *
         * @param context  Use the Application context to avoid memory leaks.
         */
        fun getInstance(context: Context): UnaRoomDatabase {
            // Return existing instance if available (fast path, no lock needed).
            return INSTANCE ?: synchronized(this) {
                // Double-check inside the lock in case another thread just created it.
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UnaRoomDatabase::class.java,
                    "unaroom_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

