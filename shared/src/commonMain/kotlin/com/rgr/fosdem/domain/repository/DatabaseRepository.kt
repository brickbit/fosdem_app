package com.rgr.fosdem.domain.repository

import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.model.bo.VideoBo

interface DatabaseRepository {
    suspend fun saveSchedule(schedule: List<ScheduleBo>)
    suspend fun getSchedule(): Result<List<ScheduleBo>>
    suspend fun saveVideos(videos: List<VideoBo>)
    suspend fun getVideos(): Result<List<VideoBo>>
}