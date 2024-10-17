package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.LocalRepository

class SaveFavouriteTracksShownUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke() {
        repository.setFavouritesTracksSeen()
    }
}