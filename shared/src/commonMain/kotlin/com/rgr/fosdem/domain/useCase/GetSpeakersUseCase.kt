package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository

class GetSpeakersUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository,
) {
    suspend operator fun invoke(): Result<List<SpeakerBo>> {
        val realmResult = realmRepository.getSchedule()
        val preferences = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        return if(preferences != null) {
            val speakers = preferences.map { it.events.map { it.speaker }.flatten() }
            Result.success(speakers.flatten().toSet().toList())
        } else {
            Result.failure(Error())
        }
    }
}