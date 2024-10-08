package com.rgr.fosdem.domain.repository

import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.model.bo.VideoBo

interface InMemoryRepository {
    fun fetchSchedules(): List<ScheduleBo>
    fun saveScheduleList(items: List<ScheduleBo>): Result<Unit>
    fun fetchVideos(): List<VideoBo>
    fun saveVideoList(items: List<VideoBo>): Result<Unit>
    fun fetchStands(): List<StandBo>
    fun saveStandList(items: List<StandBo>): Result<Unit>
    fun fetchSpeakers(): List<SpeakerBo>
    fun saveSpeakerList(items: List<SpeakerBo>): Result<Unit>
}