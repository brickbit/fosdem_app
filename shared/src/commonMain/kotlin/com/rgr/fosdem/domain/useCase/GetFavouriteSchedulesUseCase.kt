package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.repository.DatabaseRepository
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetFavouriteSchedulesUseCase(
    private val databaseRepository: DatabaseRepository
) {

    suspend operator fun invoke(): Result<List<ScheduleBo>> {
        val data = databaseRepository.getSchedule()
        data.getOrNull()?.let { schedules ->
            schedules.ifEmpty { return Result.failure(ErrorType.EmptyScheduleListError) }
            return Result.success(schedules.toMutableList().filter{ it.favourite }.sortedBy { it.date })
        } ?: return Result.failure(ErrorType.EmptyScheduleListError)
    }
}