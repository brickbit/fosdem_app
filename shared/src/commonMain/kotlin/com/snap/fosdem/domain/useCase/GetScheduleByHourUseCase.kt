package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleByHourUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(hour: String): Result<List<EventBo>> {
        val events = repository.getSchedule().getOrNull()?.let { schedules ->
            schedules.map { it.events }.flatten().filter { it.startHour == hour }
        }

        return events?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}