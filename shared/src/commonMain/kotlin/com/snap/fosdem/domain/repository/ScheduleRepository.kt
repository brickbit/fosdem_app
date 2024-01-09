package com.snap.fosdem.domain.repository

import com.snap.fosdem.domain.model.TrackBo

interface ScheduleRepository {
    suspend fun getSchedule(): Result<List<TrackBo>>
}