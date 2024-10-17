package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.LocalRepository
import com.rgr.fosdem.domain.repository.NetworkRepository

class IsUpdateNeeded(
    private val repository: NetworkRepository,
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