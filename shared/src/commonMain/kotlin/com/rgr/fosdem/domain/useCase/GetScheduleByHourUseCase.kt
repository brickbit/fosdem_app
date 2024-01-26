package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class GetScheduleByHourUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(): Result<List<EventBo>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val events = getEventsByHour(schedulesData)

        return if(events != null) {
            Result.success(events)
        } else {
            Result.success(getEventsByHour(jsonProvider.getSchedule().getOrNull())!!)
        }
    }

    private fun calculateHour(event: EventBo): Boolean {
        val nowDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val now = nowDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val day = if(event.day == "Saturday") 3 else 4
        val eventDate = "2024-02-0${day}T${event.startHour}:00.000000".toLocalDateTime()
        val eventTime = eventDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        return eventTime - now < 0.5*3600000 && eventTime - now > 0
    }

    private fun getEventsByHour(
        schedulesData: List<TrackBo>?
    ): List<EventBo>? {
        return schedulesData?.let { schedules ->
            schedules.map { it.events }.flatten().filter { event ->
                calculateHour(event)
            }.sortedBy { it.startHour }
        }
    }
}