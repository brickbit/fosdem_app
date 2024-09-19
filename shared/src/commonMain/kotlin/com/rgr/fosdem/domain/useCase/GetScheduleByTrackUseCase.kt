package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetScheduleByTrackUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: NetworkRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(trackName: String): Result<TrackBo> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val track = schedulesData?.let { schedules ->
            schedules.first { it.name == trackName }
        }
        return if(track != null) {
            Result.success(track)
        } else {
            Result.success(jsonProvider.getSchedule().getOrNull()!!.first { it.name == trackName })
        }
    }
}