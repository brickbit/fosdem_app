package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.repository.DatabaseRepository
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetNewRoomsUseCase(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        databaseRepository.getSchedule().getOrNull()?.let { schedules ->
            val rooms = schedules.map {
                it.room
            }
            rooms.ifEmpty { return Result.failure(ErrorType.NoRoomsFoundError) }
            return Result.success(rooms.distinct().sorted())
        } ?: return Result.failure(ErrorType.NoRoomsFoundError)
    }
}