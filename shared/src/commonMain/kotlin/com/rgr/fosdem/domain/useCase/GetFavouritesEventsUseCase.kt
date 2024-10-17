package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.repository.LocalRepository

class GetFavouritesEventsUseCase(
    private val repository: LocalRepository
) {
    suspend operator fun invoke(): Result<List<EventBo>> {
        return Result.success(repository.getNotificationEvents())
    }
}