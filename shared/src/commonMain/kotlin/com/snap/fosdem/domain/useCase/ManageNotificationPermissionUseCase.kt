package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.LocalRepository

class ManageNotificationPermissionUseCase (
    private val repository: LocalRepository
){
    suspend operator fun invoke(granted: Boolean): Result<Unit> {
        repository.setNotificationsPermission(granted)
        return Result.success(Unit)
    }
}