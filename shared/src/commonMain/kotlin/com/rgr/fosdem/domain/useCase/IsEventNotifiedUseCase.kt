package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.repository.LocalRepository

class IsEventNotifiedUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(event: EventBo): Boolean {
        return repository.getNotificationEvents().count { it == event } > 0
    }
}