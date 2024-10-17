package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetEventByIdUseCase(
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(id: String): Result<EventBo> {
        return Result.failure(Error())
        //return Result.success(jsonProvider.getSchedule().getOrNull()!!.map { it.events }.flatten().first { it.id == id })
    }
}