package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleByBuildingUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(building: String): Result<List<EventBo>> {
        val realmResult = realmRepository.getSchedule()
        val schedule = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val events = schedule?.let { schedules ->
            schedules.map { it.events }.flatten().filter { it.talk.room.building.name == building }
        }

        return events?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}