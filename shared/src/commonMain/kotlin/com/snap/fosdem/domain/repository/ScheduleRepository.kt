package com.snap.fosdem.domain.repository

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.model.VersionBo

interface ScheduleRepository {
    suspend fun getSchedule(): Result<List<TrackBo>>

    suspend fun getVersion(): Result<VersionBo>
}