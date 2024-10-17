package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.repository.DatabaseRepository

class GetStandsUseCase(
    private val database: DatabaseRepository,
) {
    suspend operator fun invoke(): Result<List<StandBo>> {
        database.getStands().getOrNull()?.let { standList ->
            return Result.success(standList)
        } ?: return Result.failure(ErrorType.EmptyStandListError)
    }
}