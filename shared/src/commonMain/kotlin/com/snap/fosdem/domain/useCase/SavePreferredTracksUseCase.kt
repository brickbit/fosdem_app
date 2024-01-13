package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.LocalRepository

class SavePreferredTracksUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(tracks: List<TrackBo>) {
        repository.setPreferences(tracks)
    }
}