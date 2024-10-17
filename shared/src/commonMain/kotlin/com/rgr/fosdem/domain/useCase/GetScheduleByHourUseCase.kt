package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.NetworkRepository
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class GetScheduleByHourUseCase(
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(instant: Instant): Result<List<EventBo>> {
        val schedulesData =  repository.getSchedule().getOrNull()
        val events = getEventsByHour(
            schedulesData,
            instant
        )

        return if(events != null) {
            Result.success(events)
        } else {
            Result.success(getEventsByHour(
                instant = instant,
                schedulesData = emptyList()//jsonProvider.getSchedule().getOrNull())!!,
                )!!
            )
        }
    }

    private fun calculateHour(instant: Instant, event: EventBo): Boolean {
        val nowDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val now = nowDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val day = if(event.day == "Saturday") 3 else 4
        val eventDate = "2024-02-0${day}T${event.startHour}:00.000000".toLocalDateTime()
        val eventTime = eventDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        return eventTime - now < 0.5*3600000 && eventTime - now > 0
    }

    private fun getEventsByHour(
        schedulesData: List<TrackBo>?,
        instant: Instant,
    ): List<EventBo>? {
        return schedulesData?.let { schedules ->
            schedules.map { it.events }.flatten().filter { event ->
                calculateHour(instant, event)
            }.sortedBy { it.startHour }
        }
    }
}