package com.snap.fosdem.domain.provider

interface PermissionRepository {
    suspend fun grantNotificationPermission(granted: Boolean)
    suspend fun grantLocationPermission(granted: Boolean)
}