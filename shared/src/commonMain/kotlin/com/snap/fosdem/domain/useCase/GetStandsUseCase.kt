package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.StandBo
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

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