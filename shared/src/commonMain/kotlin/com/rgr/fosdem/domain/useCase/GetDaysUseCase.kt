package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.repository.DatabaseRepository
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetDaysUseCase(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        databaseRepository.getSchedule().getOrNull()?.let { schedules ->
            val days = schedules.map { it.date }
            days.ifEmpty { return Result.failure(ErrorType.NoDaysFoundError) }
            val filteredDays = days.map { it.substring(IntRange(0, 9)) }.distinct().sorted()
            return Result.success(filteredDays)
        }?: return Result.failure(ErrorType.NoDaysFoundError)
    }
}