package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.LocalRepository

class GetOnBoardingStatusUseCase(
    private val repositoryImpl: LocalRepository
) {
    suspend operator fun invoke(): Boolean {
        return repositoryImpl.isOnBoardingSeen()
    }
}