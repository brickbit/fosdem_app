package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetDaysUseCase(
    private val inMemoryRepository: InMemoryRepository
) {
    operator fun invoke(): Result<List<String>> {
        val days = inMemoryRepository.fetchSchedules().map { it.date }
        days.ifEmpty { return Result.failure(ErrorType.NoDaysFoundError) }
        val filteredDays = days.map { it.substring(IntRange(0,9)) }.distinct().sorted()
        return Result.success(filteredDays)
    }
}