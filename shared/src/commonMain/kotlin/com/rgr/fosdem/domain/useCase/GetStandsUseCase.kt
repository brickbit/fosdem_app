package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository

class GetStandsUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository,
) {
    suspend operator fun invoke(): Result<List<StandBo>> {
        val realmResult = realmRepository.getSchedule()
        val preferences = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        return if(preferences != null) {
            val stands = preferences.map { it.stands }
            Result.success(stands.flatten().toSet().toList())
        } else {
            Result.failure(Error())
        }
    }
}