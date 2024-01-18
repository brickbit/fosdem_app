package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.repository.LocalRepository

class IsEventNotifiedUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(event: EventBo): Boolean {
        return repository.getNotificationEvents().count { it == event } > 0
    }
}