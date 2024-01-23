package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.repository.LocalRepository

class GetFavouritesEventsUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Result<List<EventBo>> {
        return Result.success(repository.getNotificationEvents())
    }
}