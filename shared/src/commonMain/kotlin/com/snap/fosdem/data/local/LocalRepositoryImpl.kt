package com.snap.fosdem.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.LocalRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): LocalRepository {

    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val IS_ON_BOARDING_SHOWN = "IS_ON_BOARDING_SHOWN"
        private const val PREFERRED_TRACK_LIST = "PREFERRED_TRACK_LIST"
    }

    private val onboardingShown = booleanPreferencesKey("$PREFS_TAG_KEY$IS_ON_BOARDING_SHOWN")
    private val preferredTracks = stringPreferencesKey("$PREFS_TAG_KEY$PREFERRED_TRACK_LIST")

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
}