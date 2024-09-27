package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetStandsUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(): Result<List<StandBo>> {
            val stands = jsonProvider.getSchedule().getOrNull()!!.map { it.stands }
            return Result.success(stands.flatten().toSet().toList())
    }
}