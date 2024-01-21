package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleByTrackUseCase(
    private val repository: ScheduleRepository,
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
        return track?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}