package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetEventByIdUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(id: String): Result<EventBo> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val event = schedulesData?.let { schedules ->
            schedules.map { it.events }.flatten().first { it.id == id }
        }

        return event?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}