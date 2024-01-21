package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetTracksUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository,
) {
    suspend operator fun invoke(): Result<List<TrackBo>> {
        val realmResult = realmRepository.getSchedule()
        val preferences = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        return if(preferences != null) {
            Result.success(preferences)
        } else {
            Result.failure(Error())
        }
    }
}