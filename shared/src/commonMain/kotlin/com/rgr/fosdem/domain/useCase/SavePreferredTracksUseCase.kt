package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.LocalRepository

class SavePreferredTracksUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(tracks: List<TrackBo>) {
        repository.setPreferences(tracks)
    }
}