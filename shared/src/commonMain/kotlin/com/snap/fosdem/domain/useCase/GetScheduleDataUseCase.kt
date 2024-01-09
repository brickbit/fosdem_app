package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleDataUseCase(
    private var repository: ScheduleRepository
) {

    suspend operator fun invoke(): Result<List<TrackBo>>  {
        return repository.getSchedule()
    }
}