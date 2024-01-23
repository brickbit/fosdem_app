package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetScheduleDataUseCase(
    private var networkRepository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {

    suspend operator fun invoke(needUpdate: Boolean): Result<List<TrackBo>>  {
        val realmResult = realmRepository.getSchedule()
        return if(realmResult.isNotEmpty()) {
            Result.success(realmResult)
        } else {
            val ktorResult = networkRepository.getSchedule()
            if(ktorResult.isSuccess) {
                realmRepository.saveSchedule(ktorResult.getOrNull()!!)
            }
            ktorResult
        }
    }
}