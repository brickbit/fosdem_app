package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.LocalRepository

class ManageNotificationPermissionUseCase (
    private val repository: LocalRepository
){
    suspend operator fun invoke(granted: Boolean): Result<Unit> {
        repository.setNotificationsPermission(granted)
        return Result.success(Unit)
    }
}