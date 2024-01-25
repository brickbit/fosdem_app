package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.NotificationBo
import com.rgr.fosdem.domain.repository.LocalRepository

class GetEventsForNotificationUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Result<NotificationBo> {
        val events = repository.getNotificationEvents()
        return Result.success(
            NotificationBo(
                events = events,
                time = repository.getNotificationTime(),
            )
        )
    }
}