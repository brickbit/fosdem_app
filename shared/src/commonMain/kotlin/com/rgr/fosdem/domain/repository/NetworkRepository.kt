package com.rgr.fosdem.domain.repository

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.VersionBo

interface NetworkRepository {
    suspend fun getSchedule(): Result<List<TrackBo>>

    suspend fun getVersion(): Result<VersionBo>

    suspend fun loadScheduleData(): Result<String>

    suspend fun loadStandsData(): Result<String>

    suspend fun loadSpeakersData(): Result<String>

    suspend fun loadSpeakerItemData(speakerEndpoint: String): Result<String>
}