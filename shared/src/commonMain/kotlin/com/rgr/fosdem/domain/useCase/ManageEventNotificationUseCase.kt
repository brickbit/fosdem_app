package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.repository.LocalRepository

class ManageEventNotificationUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(event: EventBo, enable: Boolean) {
        if(enable) {
            repository.addNotificationForEvent(event)
        } else {
            repository.removeNotificationForEvent(event)
        }

    }
}