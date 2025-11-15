package com.allsoundspro.data.di

import android.content.Context
import androidx.room.Room
import com.allsoundspro.core.utils.Constants
import com.allsoundspro.data.database.AppDatabase
import com.allsoundspro.data.repository.SoundRepositoryImpl
import com.allsoundspro.domain.repository.ISoundRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSoundRepository(
        // database: AppDatabase // Uncomment when DAOs are added
    ): ISoundRepository {
        return SoundRepositoryImpl()
    }
}
