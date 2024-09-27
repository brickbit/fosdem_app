package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetScheduleDataUseCase(
    private var jsonRepository: JsonProvider,
    private var networkRepository: NetworkRepository,
) {

    suspend operator fun invoke(): Result<List<TrackBo>>  {
        return jsonRepository.getSchedule()
    }
}