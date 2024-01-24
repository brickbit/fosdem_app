package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.LocalRepository

class SaveFavouriteTracksShownUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke() {
        repository.setFavouritesTracksSeen()
    }
}