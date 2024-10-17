package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.VideoBo
import com.rgr.fosdem.domain.repository.DatabaseRepository
import kotlinx.coroutines.delay

class GetVideoUseCase(
    private val databaseRepository: DatabaseRepository
) {

    suspend operator fun invoke(): Result<List<VideoBo>> {
        databaseRepository.getVideos().getOrNull()?.let { videos ->
            videos.ifEmpty { return Result.failure(ErrorType.EmptyVideoListError) }
            return Result.success(videos)
        } ?: return Result.failure(ErrorType.EmptyVideoListError)
    }
}