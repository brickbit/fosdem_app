package com.rgr.fosdem.domain.repository

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.VersionBo

interface ScheduleRepository {
    suspend fun getSchedule(): Result<List<TrackBo>>

    suspend fun getVersion(): Result<VersionBo>
}