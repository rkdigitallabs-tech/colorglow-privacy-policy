package com.allsoundspro.data.repository

import com.allsoundspro.domain.model.Sound
import com.allsoundspro.domain.repository.ISoundRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Implementation of Sound Repository
 * Currently returns empty data - will be implemented with Room database
 */
class SoundRepositoryImpl @Inject constructor(
    // Inject DAOs here when created
) : ISoundRepository {

    override suspend fun getAllSounds(): Flow<List<Sound>> {
        // TODO: Implement with Room database
        return flowOf(emptyList())
    }

    override suspend fun getSoundById(id: Long): Sound? {
        // TODO: Implement with Room database
        return null
    }

    override suspend fun insertSound(sound: Sound) {
        // TODO: Implement with Room database
    }

    override suspend fun updateSound(sound: Sound) {
        // TODO: Implement with Room database
    }

    override suspend fun deleteSound(sound: Sound) {
        // TODO: Implement with Room database
    }

    override suspend fun getFavoriteSounds(): Flow<List<Sound>> {
        // TODO: Implement with Room database
        return flowOf(emptyList())
    }
}
