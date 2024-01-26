package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository

class GetScheduleDataUseCase(
    private var jsonRepository: JsonProvider,
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
                ktorResult
            } else {
                jsonRepository.getSchedule()
            }
        }
    }
}