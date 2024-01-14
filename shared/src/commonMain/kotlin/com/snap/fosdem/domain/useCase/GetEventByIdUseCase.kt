package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetEventByIdUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(id: String): Result<EventBo> {
        val event = repository.getSchedule().getOrNull()?.let { schedules ->
            schedules.map { it.events }.flatten().first { it.id == id }
        }

        return event?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}