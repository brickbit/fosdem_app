package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.model.VersionBo
import com.snap.fosdem.domain.repository.LocalRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class IsUpdateNeeded(
    private val repository: ScheduleRepository,
    private val localRepository: LocalRepository,
) {
    suspend operator fun invoke(): Result<Boolean> {
        val version = repository.getVersion().getOrNull()
        val localVersion = localRepository.getVersion()
        version?.let {
            if(it.version != localVersion) {
                localRepository.saveVersion(it.version)
                return Result.success(true)
            } else {
                return Result.success(false)
            }
        } ?: return Result.failure(Error())
    }
}