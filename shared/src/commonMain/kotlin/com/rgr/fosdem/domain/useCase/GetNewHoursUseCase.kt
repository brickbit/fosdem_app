package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetNewHoursUseCase(
    private val inMemoryRepository: InMemoryRepository
) {
    operator fun invoke(): Result<List<String>> {
        val hours = inMemoryRepository.fetchSchedules().map { it.start }
        hours.ifEmpty { return Result.failure(ErrorType.NoHoursFoundError) }
        return Result.success(hours.distinct().sorted())
    }
}