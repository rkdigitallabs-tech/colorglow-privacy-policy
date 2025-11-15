package com.allsoundspro.domain.repository

import com.allsoundspro.domain.model.Sound
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for sound data operations
 */
interface ISoundRepository {
    suspend fun getAllSounds(): Flow<List<Sound>>
    suspend fun getSoundById(id: Long): Sound?
    suspend fun insertSound(sound: Sound)
    suspend fun updateSound(sound: Sound)
    suspend fun deleteSound(sound: Sound)
    suspend fun getFavoriteSounds(): Flow<List<Sound>>
}
