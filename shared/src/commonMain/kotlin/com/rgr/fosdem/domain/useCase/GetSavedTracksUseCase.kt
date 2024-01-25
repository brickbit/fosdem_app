package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.LocalRepository

class GetSavedTracksUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke (): List<TrackBo> {
        return repository.getPreferences()
    }
}