package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.VideoBo
import com.rgr.fosdem.domain.repository.InMemoryRepository
import kotlinx.coroutines.delay

class GetVideoUseCase(
    private val inMemoryRepository: InMemoryRepository
) {

    suspend operator fun invoke(): Result<List<VideoBo>> {
        delay(3000)
        val videos = inMemoryRepository.fetchVideos()
        videos.ifEmpty { return Result.failure(ErrorType.EmptyVideoListError) }
        return Result.success(videos)
    }
}