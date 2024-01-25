package com.rgr.fosdem.data.repository

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.VersionBo
import com.rgr.fosdem.domain.repository.JsonRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository

class JsonRepositoryImpl: JsonRepository {

    override suspend fun getSchedule(): Result<List<TrackBo>> {
        return Result.success(listOf())
    }

    override suspend fun getVersion(): Result<VersionBo> {
        return Result.success(VersionBo("0.0.1",""))
    }

}