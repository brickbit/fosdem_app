package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.LocalRepository

class GetPreferredTracksShownUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isFavouriteTracksSeen()
    }
}