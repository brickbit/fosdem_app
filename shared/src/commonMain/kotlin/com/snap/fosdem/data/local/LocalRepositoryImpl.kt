package com.snap.fosdem.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.snap.fosdem.domain.repository.LocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): LocalRepository {

    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val IS_ON_BOARDING_SHOWN = "IS_ON_BOARDING_SHOWN"
    }

    private val onboardingShown = booleanPreferencesKey("$PREFS_TAG_KEY$IS_ON_BOARDING_SHOWN")

    override suspend fun setOnBoardingSeen() = dataStore.edit { preferences ->
        preferences[onboardingShown] = true
    }
    override suspend fun isOnBoardingSeen() = dataStore.data.map { preferences ->
        preferences[onboardingShown] ?: false
    }.first()

    override suspend fun setPreferences(preferences: List<String>) {

    }
    override suspend fun getPreferences(): List<String> {
        return emptyList()
    }
}