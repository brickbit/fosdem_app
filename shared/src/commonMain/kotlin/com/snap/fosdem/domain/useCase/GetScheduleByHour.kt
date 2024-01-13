package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.BuildingBo
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleByHour(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(hour: String): Result<List<TrackBo>> {
        val track = repository.getSchedule().getOrNull()?.let { schedules ->
            schedules.filter { track ->
                track.events.count { event ->
                    event.talk?.start == hour
                } > 0
            }
        }

        return track?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}