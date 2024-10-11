package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.repository.DatabaseRepository

class GetNewTracksUseCase(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        databaseRepository.getSchedule().getOrNull()?.let { schedules ->
            val tracks = schedules.map { it.track }
            tracks.ifEmpty { return Result.failure(ErrorType.NoTracksFoundError) }
            return Result.success(tracks.distinct().sorted())
        } ?: return Result.failure(ErrorType.NoTracksFoundError)
    }
}