package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleByTrack(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(trackName: String): Result<TrackBo> {
        val track = repository.getSchedule().getOrNull()?.let { schedules ->
            schedules.first { it.name == trackName }
        }
        return track?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}