package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository

class GetEventByIdUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(id: String): Result<EventBo> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val event = schedulesData?.let { schedules ->
            schedules.map { it.events }.flatten().first { it.id == id }
        }

        return if(event != null) {
            Result.success(event)
        } else {
            Result.success(jsonProvider.getSchedule().getOrNull()!!.map { it.events }.flatten().first { it.id == id })
        }
    }
}