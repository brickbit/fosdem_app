package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleByParameterUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(
        day: String = "Saturday",
        hours: List<String> = emptyList(),
        tracks: List<String> = emptyList(),
        rooms: List<String> = emptyList(),
    ): Result<List<EventBo>> {
        val events = repository.getSchedule().getOrNull()?.let { schedules ->
            val allEvents = schedules.map { it.events }.flatten()
            val eventsFilterByDay = allEvents.filter { it.day == day }
            val filterDayHour = if (hours.isNotEmpty()) eventsFilterByDay.filter { hours.contains(it.startHour) } else eventsFilterByDay
            val filterDayHourTrack = if (tracks.isNotEmpty()) filterDayHour.filter { tracks.contains(it.talk?.track ?: "") } else filterDayHour
            if (rooms.isNotEmpty()) filterDayHourTrack.filter { rooms.contains(it.talk?.room ?: "") } else filterDayHourTrack
        }
        return events?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}