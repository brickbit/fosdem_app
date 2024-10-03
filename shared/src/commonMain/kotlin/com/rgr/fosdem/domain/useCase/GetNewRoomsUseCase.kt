package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetNewRoomsUseCase(
    private val inMemoryRepository: InMemoryRepository
) {
    operator fun invoke(): Result<List<String>> {
        val rooms = inMemoryRepository.fetchSchedules().map {
            it.room
        }
        rooms.ifEmpty { return Result.failure(ErrorType.NoRoomsFoundError) }
        return Result.success(rooms.distinct().sorted())
    }
}