package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.repository.DatabaseRepository

class GetRightNowSchedulesUseCase(
    private val databaseRepository: DatabaseRepository
) {

    suspend operator fun invoke(): Result<List<ScheduleBo>> {
        databaseRepository.getSchedule().getOrNull()?.let { data ->
            data.ifEmpty { return Result.failure(ErrorType.EmptyScheduleListError) }
            return Result.success(data.toMutableList().filter { it.start == "0" }
                .sortedBy { it.date })
        } ?: return Result.failure(ErrorType.EmptyScheduleListError)
    }
}