package com.rgr.fosdem.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.rgr.fosdem.app.navigation.Routes
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.TrackBo

interface LocalRepository {
    suspend fun setOnBoardingSeen(): Preferences
    suspend fun isOnBoardingSeen(): Boolean

    suspend fun setFavouritesTracksSeen(): Preferences
    suspend fun isFavouriteTracksSeen(): Boolean

    suspend fun setPreferences(preferences: List<TrackBo>)
    suspend fun getPreferences(): List<TrackBo>

    suspend fun setNotificationsPermission(permission: Boolean): Preferences
    suspend fun getNotificationsPermission(): Boolean

    suspend fun addNotificationForEvent(eventBo: EventBo)
    suspend fun removeNotificationForEvent(eventBo: EventBo)
    suspend fun getNotificationEvents(): List<EventBo>

    suspend fun setNotificationTime(time: Int): Preferences
    suspend fun getNotificationTime(): Int

    suspend fun saveVersion(version: String): Preferences
    suspend fun getVersion(): String
}