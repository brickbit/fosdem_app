package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetScheduleDataUseCase(
    private var networkRepository: NetworkRepository,
) {

    suspend operator fun invoke(): Result<List<TrackBo>>  {
        return Result.failure(Error())
        //return jsonRepository.getSchedule()
    }
}