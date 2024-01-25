package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.BuildingBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.ScheduleRepository

class GetScheduleByBuilding(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(building: BuildingBo): Result<List<TrackBo>> {
        val track = repository.getSchedule().getOrNull()?.let { schedules ->
            schedules.filter { track ->
                track.events.count { event ->
                    event.talk?.room?.building == building
                } > 0
            }
        }

        return track?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}