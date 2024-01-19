package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.NotificationBo
import com.snap.fosdem.domain.repository.LocalRepository

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