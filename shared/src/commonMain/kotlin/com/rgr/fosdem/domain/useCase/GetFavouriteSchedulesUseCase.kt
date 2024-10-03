package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetFavouriteSchedulesUseCase(
    private val inMemoryRepository: InMemoryRepository
) {

    operator fun invoke(): Result<List<ScheduleBo>> {
        val data = inMemoryRepository.fetchSchedules()
        data.ifEmpty { return Result.failure(ErrorType.EmptyScheduleListError) }
        return Result.success(data.toMutableList().filter{ it.favourite }.sortedBy { it.date })
    }
}