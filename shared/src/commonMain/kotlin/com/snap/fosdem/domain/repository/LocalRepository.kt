package com.snap.fosdem.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun setOnBoardingSeen()
    suspend fun isOnBoardingSeen(): Flow<Boolean>

    suspend fun setPreferences(preferences: List<String>)
    suspend fun getPreferences(): Flow<List<String>>
}