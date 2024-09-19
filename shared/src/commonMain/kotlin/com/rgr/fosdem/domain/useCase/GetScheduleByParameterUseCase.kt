package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetScheduleByParameterUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: NetworkRepository,
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
        val events = getEventsByParameter(
            schedulesData, day, hours, track, room
        )

        return if(events != null) {
            Result.success(events.sortedBy { item -> item.startHour })
        } else {
            val eventsJson = getEventsByParameter(
                jsonProvider.getSchedule().getOrNull(), day, hours, track, room
            )
            Result.success(eventsJson!!.sortedBy { item -> item.startHour })
        }
    }

    private fun getEventsByParameter(
        schedulesData: List<TrackBo>?,
        day: String,
        hours: List<String>,
        track: String,
        room: String,
    ): List<EventBo>? {
        return schedulesData?.let { schedules ->
            val allEvents = schedules.map { it.events }.flatten()
            val eventsFilterByDay = allEvents.filter { it.day == day }
            val filterDayHour = if (hours.isNotEmpty()) eventsFilterByDay.filter { hours.contains(it.startHour) } else eventsFilterByDay
            val filterDayHourTrack = if (track.isNotEmpty() && track != "All") filterDayHour.filter { track == it.talk.track } else filterDayHour
            if (room.isNotEmpty() && room != "All") filterDayHourTrack.filter { room == it.talk.room.name } else filterDayHourTrack
        }
    }
}