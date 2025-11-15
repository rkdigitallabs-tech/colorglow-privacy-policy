package com.allsoundspro.domain.usecase

import com.allsoundspro.domain.model.Sound
import com.allsoundspro.domain.repository.ISoundRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving sounds
 */
class GetSoundsUseCase @Inject constructor(
    private val repository: ISoundRepository
) {
    suspend operator fun invoke(): Flow<List<Sound>> {
        return repository.getAllSounds()
    }
}
