package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.LocalRepository

class ManageNotificationTimeUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(time: Int): Result<Unit> {
        repository.setNotificationTime(time)
        return Result.success(Unit)
    }
}