package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetScheduleByBuildingUseCase(
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(building: String): Result<List<EventBo>> {
        val schedule = repository.getSchedule().getOrNull()

        val events = schedule?.let { schedules ->
            schedules.map { it.events }.flatten().filter { it.talk.room.building.name == building }
        }

        return events?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}