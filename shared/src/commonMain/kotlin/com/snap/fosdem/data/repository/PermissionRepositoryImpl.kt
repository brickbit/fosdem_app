package com.snap.fosdem.data.repository

import com.snap.fosdem.domain.provider.PermissionRepository
import com.snap.fosdem.domain.repository.LocalRepository

class PermissionRepositoryImpl(
    private val localRepository: LocalRepository,
): PermissionRepository {

    override suspend fun grantNotificationPermission(granted: Boolean) {
        localRepository.setNotificationsPermission(true)
    }
}