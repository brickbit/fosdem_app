package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.repository.DatabaseRepository

class GetNewHoursUseCase(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        databaseRepository.getSchedule().getOrNull()?.let { schedule ->
            val hours = schedule.map { it.start }
            hours.ifEmpty { return Result.failure(ErrorType.NoHoursFoundError) }
            return Result.success(hours.distinct().sorted())
        } ?: return Result.failure(ErrorType.NoHoursFoundError)
    }
}