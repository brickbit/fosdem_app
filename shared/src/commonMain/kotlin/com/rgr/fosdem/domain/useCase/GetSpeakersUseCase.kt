package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetSpeakersUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(): Result<List<SpeakerBo>> {
            val jsonResult = jsonProvider.getSchedule()
            val speakers = jsonResult.getOrNull()!!.map { it.events.map {event -> event.speaker }.flatten() }
            return Result.success(speakers.flatten().toSet().toList())
    }
}