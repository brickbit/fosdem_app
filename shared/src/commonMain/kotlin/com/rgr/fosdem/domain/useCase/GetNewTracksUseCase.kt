package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetNewTracksUseCase(
    private val inMemoryRepository: InMemoryRepository
) {
    operator fun invoke(): Result<List<String>> {
        val tracks = inMemoryRepository.fetchSchedules().map { it.track }
        tracks.ifEmpty { return Result.failure(ErrorType.NoTracksFoundError) }
        return Result.success(tracks.distinct().sorted())
    }
}