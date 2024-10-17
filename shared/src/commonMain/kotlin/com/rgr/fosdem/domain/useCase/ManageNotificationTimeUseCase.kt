package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.LocalRepository

class ManageNotificationTimeUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(time: Int): Result<Unit> {
        repository.setNotificationTime(time)
        return Result.success(Unit)
    }
}