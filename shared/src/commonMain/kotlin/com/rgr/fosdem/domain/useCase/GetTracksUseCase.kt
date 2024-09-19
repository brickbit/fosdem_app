package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetTracksUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: NetworkRepository,
    private var realmRepository: RealmRepository,
) {
    suspend operator fun invoke(): Result<List<TrackBo>> {
        val realmResult = realmRepository.getSchedule()
        val preferences = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        return if(preferences != null) {
            Result.success(preferences)
        } else {
            return jsonProvider.getSchedule()
        }
    }
}