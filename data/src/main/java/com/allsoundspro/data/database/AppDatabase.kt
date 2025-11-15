package com.allsoundspro.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Main Room database for AllSoundsPro
 * Add entities here as they are created
 */
@Database(
    entities = [], // Add entities here when created
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // Add DAOs here as they are created
    // Example: abstract fun soundDao(): SoundDao
}
