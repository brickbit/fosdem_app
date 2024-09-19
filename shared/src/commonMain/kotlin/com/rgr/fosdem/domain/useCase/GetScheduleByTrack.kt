package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetScheduleByTrack(
    private val repository: NetworkRepository
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