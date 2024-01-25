package com.snap.fosdem.data.repository

import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.model.VersionBo
import com.snap.fosdem.domain.repository.JsonRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class JsonRepositoryImpl: JsonRepository {

    override suspend fun getSchedule(): Result<List<TrackBo>> {
        return Result.success(listOf())
    }

    override suspend fun getVersion(): Result<VersionBo> {
        return Result.success(VersionBo("0.0.1",""))
    }

}