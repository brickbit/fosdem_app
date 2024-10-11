package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.repository.DatabaseRepository

class SetFavouriteUseCase(
    private val databaseRepository: DatabaseRepository
) {

    suspend operator fun invoke(
        schedule: ScheduleBo,
        favourite: Boolean
    ): Result<Unit> {
        databaseRepository.updateSchedule(schedule.copy(favourite = favourite))
        return Result.success(Unit)
    }
}