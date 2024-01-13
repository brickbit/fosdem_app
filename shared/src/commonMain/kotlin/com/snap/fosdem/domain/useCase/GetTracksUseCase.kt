package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetTracksUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): Result<List<TrackBo>> {
        val preferences = repository.getSchedule().getOrNull()
        return if(preferences != null) {
            Result.success(preferences)
        } else {
            Result.failure(Error())
        }
    }
}