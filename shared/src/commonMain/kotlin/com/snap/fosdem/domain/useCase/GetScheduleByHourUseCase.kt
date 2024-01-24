package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class GetScheduleByHourUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(): Result<List<EventBo>> {


        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val events = schedulesData?.let { schedules ->
             schedules.map { it.events }.flatten().filter { event ->
                calculateHour(event)
            }.sortedBy { it.startHour }
        }

        return events?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }

    private fun calculateHour(event: EventBo): Boolean {
        val nowDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val now = nowDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val day = if(event.day == "Saturday") 3 else 4
        val eventDate = "2024-02-0${day}T${event.startHour}:00.000000".toLocalDateTime()
        val eventTime = eventDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        return eventTime - now < 0.5*3600000 && eventTime - now > 0
    }
}