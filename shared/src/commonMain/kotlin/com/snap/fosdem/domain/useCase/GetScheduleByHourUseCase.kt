package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class GetScheduleByHourUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(hour: Long): Result<List<EventBo>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val events = schedulesData?.let { schedules ->
            schedules.map { it.events }.flatten().filter {
                val day = if(it.day == "Saturday") 3 else 4
                val eventTime = "2024-02-0${day}T22:${it.startHour}".toLocalDateTime().toInstant(
                    TimeZone.UTC).toEpochMilliseconds()
                eventTime - hour < 1*60*1000 && eventTime - hour > 0
            }
        }

        return events?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}