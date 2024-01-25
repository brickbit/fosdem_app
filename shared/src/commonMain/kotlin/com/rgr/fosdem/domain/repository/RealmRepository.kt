package com.rgr.fosdem.domain.repository

import com.rgr.fosdem.domain.model.TrackBo

interface RealmRepository {

    suspend fun getSchedule(): List<TrackBo>

    suspend fun saveSchedule(schedules: List<TrackBo>)

}