package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.LocalRepository

class GetPreferredTracksUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): List<TrackBo> {
        return repository.getPreferences()
    }
}