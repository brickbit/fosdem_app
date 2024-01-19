package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.LocalRepository

class GetNotificationTimeUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getNotificationTime()
    }
}