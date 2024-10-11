package com.rgr.fosdem.data.dataSource.db.repository

import com.rgr.fosdem.data.dataSource.db.AppDatabase
import com.rgr.fosdem.data.dataSource.db.entity.toBo
import com.rgr.fosdem.data.dataSource.db.entity.toEntity
import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.model.bo.VideoBo
import com.rgr.fosdem.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.first

class DatabaseRepositoryImpl(
    private val db: AppDatabase
): DatabaseRepository {

    override suspend fun saveSchedule(schedule: List<ScheduleBo>) {
        db.scheduleDao().save(schedule.map { it.toEntity()})
    }

    override suspend fun getSchedule(): Result<List<ScheduleBo>> {
        val data = db.scheduleDao().fetchSchedules().first().map { it.toBo() }
        return Result.success(data)
    }

    override suspend fun updateSchedule(schedule: ScheduleBo) {
        db.scheduleDao().updateSchedule(schedule.toEntity())
    }

    override suspend fun saveVideos(videos: List<VideoBo>) {
        db.videoDao().save(videos.map {it.toEntity()})
    }

    override suspend fun getVideos(): Result<List<VideoBo>> {
        val data = db.videoDao().fetchVideos().first().map { it.toBo() }
        return Result.success(data)
    }

    override suspend fun saveStands(stands: List<StandBo>) {
        db.standDao().save(stands.map {it.toEntity()})
    }

    override suspend fun getStands(): Result<List<StandBo>> {
        val data = db.standDao().fetchStands().first().map { it.toBo() }
        return Result.success(data)
    }

    override suspend fun saveSpeakers(speakers: List<SpeakerBo>) {
        TODO("Not yet implemented")
    }

    override suspend fun getSpeakers(): Result<List<SpeakerBo>> {
        TODO("Not yet implemented")
    }
}