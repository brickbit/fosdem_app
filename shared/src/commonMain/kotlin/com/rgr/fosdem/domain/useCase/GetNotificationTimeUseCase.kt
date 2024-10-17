package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.LocalRepository

class GetNotificationTimeUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getNotificationTime()
    }
}