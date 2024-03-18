package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.LocalRepository

class GetOnBoardingStatusUseCase(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(): Boolean {
        return localRepository.isOnBoardingSeen()
    }
}