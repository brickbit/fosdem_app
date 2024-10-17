package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetTracksUseCase(
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(): Result<List<TrackBo>> {
        return Result.failure(Error())
        //return jsonProvider.getSchedule()
    }
}