package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleByParameterUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(
        day: String,
        hours: List<String>,
        track: String,
        room: String,
    ): Result<List<EventBo>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val events = schedulesData?.let { schedules ->
            val allEvents = schedules.map { it.events }.flatten()
            val eventsFilterByDay = allEvents.filter { it.day == day }
            val filterDayHour = if (hours.isNotEmpty()) eventsFilterByDay.filter { hours.contains(it.startHour) } else eventsFilterByDay
            val filterDayHourTrack = if (track.isNotEmpty() && track != "All") filterDayHour.filter { track == it.talk.track } else filterDayHour
            if (room.isNotEmpty() && room != "All") filterDayHourTrack.filter { room == it.talk.room.name } else filterDayHourTrack
        }
        return events?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}