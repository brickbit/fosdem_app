package com.rgr.fosdem.domain.repository

import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.model.bo.VideoBo

interface DatabaseRepository {
    suspend fun saveSchedule(schedule: ScheduleBo)
    suspend fun getSchedule(): Result<ScheduleBo>
    suspend fun saveVideos(video: VideoBo)
    suspend fun getVideos(): Result<VideoBo>
}