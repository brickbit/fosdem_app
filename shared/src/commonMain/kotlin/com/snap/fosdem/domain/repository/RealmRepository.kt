package com.snap.fosdem.domain.repository

import com.snap.fosdem.domain.model.TrackBo

interface RealmRepository {

    suspend fun getSchedule(): List<TrackBo>

    suspend fun saveSchedule(schedules: List<TrackBo>)

}