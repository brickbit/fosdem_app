package com.snap.fosdem.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.LocalRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): LocalRepository {

    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val IS_ON_BOARDING_SHOWN = "IS_ON_BOARDING_SHOWN"
        private const val PREFERRED_TRACK_LIST = "PREFERRED_TRACK_LIST"
        private const val NOTIFICATIONS_ENABLED = "NOTIFICATIONS_ENABLED"
        private const val EVENT_NOTIFICATIONS = "EVENT_NOTIFICATIONS"
        private const val NOTIFICATION_TIME = "NOTIFICATION_TIME"
    }

    private val onboardingShown = booleanPreferencesKey("$PREFS_TAG_KEY$IS_ON_BOARDING_SHOWN")
    private val preferredTracks = stringPreferencesKey("$PREFS_TAG_KEY$PREFERRED_TRACK_LIST")
    private val notificationsPreferences = booleanPreferencesKey("$PREFS_TAG_KEY$NOTIFICATIONS_ENABLED")
    private val eventNotificationPreferences = stringPreferencesKey("$PREFS_TAG_KEY$EVENT_NOTIFICATIONS")
    private val notificationTime = intPreferencesKey("$PREFS_TAG_KEY$NOTIFICATION_TIME")

    override suspend fun setOnBoardingSeen() = dataStore.edit { preferences ->
        preferences[onboardingShown] = true
    }
    override suspend fun isOnBoardingSeen() = dataStore.data.map { preferences ->
        preferences[onboardingShown] ?: false
    }.first()

    override suspend fun setPreferences(preferences: List<TrackBo>) {
        val list: Array<TrackBo> = preferences.toTypedArray()
        val listString = Json.encodeToString(list)
        dataStore.edit { store ->
            store[preferredTracks] = listString
        }
    }
    override suspend fun getPreferences(): List<TrackBo> {
        val stringTracks = dataStore.data.map { store ->
            store[preferredTracks] ?: ""
        }.first()
        return if (stringTracks.isNotEmpty()) {
            val arrayTracks = Json.decodeFromString<Array<TrackBo>>(stringTracks)
            arrayTracks.toList()
        } else {
            emptyList()
        }
    }

    override suspend fun setNotificationsPermission(permission: Boolean) = dataStore.edit { preferences ->
        preferences[notificationsPreferences] = permission
    }

    override suspend fun getNotificationsPermission(): Boolean = dataStore.data.map { preferences ->
        preferences[notificationsPreferences] ?: false
    }.first()

    override suspend fun addNotificationForEvent(eventBo: EventBo) {
        val list = getNotificationEvents().toMutableList()
        list.add(eventBo)
        val listString = Json.encodeToString(list)
        dataStore.edit { store ->
            store[eventNotificationPreferences] = listString
        }
    }

    override suspend fun removeNotificationForEvent(eventBo: EventBo) {
        val list = getNotificationEvents().toMutableList()
        list.remove(eventBo)
        val listString = Json.encodeToString(list)
        dataStore.edit { store ->
            store[eventNotificationPreferences] = listString
        }
    }

    override suspend fun getNotificationEvents(): List<EventBo> {
        val stringListEvents = dataStore.data.map { store ->
            store[eventNotificationPreferences] ?: ""
        }.first()
        return if (stringListEvents.isNotEmpty()) {
            val arrayTracks = Json.decodeFromString<Array<EventBo>>(stringListEvents)
            arrayTracks.toList()
        } else {
            emptyList()
        }
    }

    override suspend fun setNotificationTime(time: Int) = dataStore.edit { preferences ->
        preferences[notificationTime] = time
    }

    override suspend fun getNotificationTime() = dataStore.data.map { preferences ->
        preferences[notificationTime] ?: 10
    }.first()
}