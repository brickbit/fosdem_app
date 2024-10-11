package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetStandsUseCase(
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(): Result<List<StandBo>> {
        return Result.failure(Error())
            //val stands = jsonProvider.getSchedule().getOrNull()!!.map { it.stands }
            //return Result.success(stands.flatten().toSet().toList())
    }
}