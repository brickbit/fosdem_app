package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetScheduleByTrackUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(trackName: String): Result<TrackBo> {
        return Result.success(jsonProvider.getSchedule().getOrNull()!!.first { it.name == trackName })
    }
}