package com.snap.fosdem.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.snap.fosdem.domain.model.TrackBo

interface LocalRepository {
    suspend fun setOnBoardingSeen(): Preferences
    suspend fun isOnBoardingSeen(): Boolean

    suspend fun setPreferences(preferences: List<TrackBo>)
    suspend fun getPreferences(): List<TrackBo>

    suspend fun setNotificationsPermission(permission: Boolean): Preferences
    suspend fun getNotificationsPermission(): Boolean

}