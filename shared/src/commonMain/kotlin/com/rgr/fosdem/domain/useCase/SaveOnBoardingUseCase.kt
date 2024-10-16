package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.LocalRepository

class SaveOnBoardingUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return Result.success(repository.setOnBoardingSeen())
    }
}