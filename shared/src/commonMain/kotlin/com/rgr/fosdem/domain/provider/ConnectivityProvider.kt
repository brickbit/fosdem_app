package com.rgr.fosdem.domain.provider

import kotlinx.coroutines.flow.Flow

interface ConnectivityProvider {
    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}