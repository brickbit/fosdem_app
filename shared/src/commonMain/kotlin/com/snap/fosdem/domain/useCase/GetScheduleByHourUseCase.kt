package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleByHourUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(hour: String): Result<List<EventBo>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val events = schedulesData?.let { schedules ->
            schedules.map { it.events }.flatten().filter { it.startHour == hour }
        }

        return events?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}