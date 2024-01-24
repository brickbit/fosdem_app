package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.LocalRepository

class GetPreferredTracksShownUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isFavouriteTracksSeen()
    }
}