package com.snap.fosdem.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun setOnBoardingSeen(): Preferences
    suspend fun isOnBoardingSeen(): Boolean

    suspend fun setPreferences(preferences: List<String>)
    suspend fun getPreferences(): List<String>
}