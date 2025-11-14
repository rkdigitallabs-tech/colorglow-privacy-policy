package com.allstatusstudio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.allstatusstudio.data.models.CaptionModel
import com.allstatusstudio.data.models.SchedulerModel
import com.allstatusstudio.data.models.StatusModel
import com.allstatusstudio.data.models.TemplateModel

@Database(
    entities = [
        StatusModel::class,
        TemplateModel::class,
        CaptionModel::class,
        SchedulerModel::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
    abstract fun templatesDao(): TemplatesDao
    abstract fun captionsDao(): CaptionsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
