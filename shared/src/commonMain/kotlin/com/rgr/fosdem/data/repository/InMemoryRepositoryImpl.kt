package com.rgr.fosdem.data.repository

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.model.bo.VideoBo
import com.rgr.fosdem.domain.repository.InMemoryRepository

class InMemoryRepositoryImpl: InMemoryRepository {
    var schedules = listOf<ScheduleBo>()
    var videos = listOf<VideoBo>()

    override fun fetchSchedules(): List<ScheduleBo> {
        return schedules
    }

    override fun saveScheduleList(items: List<ScheduleBo>): Result<Unit> {
        items.ifEmpty { return Result.failure(ErrorType.EmptyScheduleListError) }
        schedules = items
        return Result.success(Unit)
    }

    override fun fetchVideos(): List<VideoBo> {
        return videos
    }

    override fun saveVideoList(items: List<VideoBo>): Result<Unit> {
        items.ifEmpty { return Result.failure(ErrorType.EmptyVideoListError) }
        videos = items
        return Result.success(Unit)
    }
}