package com.rgr.fosdem.domain.repository

import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.model.bo.VideoBo

interface DatabaseRepository {
    suspend fun saveSchedule(schedule: List<ScheduleBo>)
    suspend fun getSchedule(): Result<List<ScheduleBo>>
    suspend fun updateSchedule(schedule: ScheduleBo)
    suspend fun saveVideos(videos: List<VideoBo>)
    suspend fun getVideos(): Result<List<VideoBo>>
    suspend fun saveStands(stands: List<StandBo>)
    suspend fun getStands(): Result<List<StandBo>>
    suspend fun saveSpeakers(speakers: List<SpeakerBo>)
    suspend fun getSpeakers(): Result<List<SpeakerBo>>
}