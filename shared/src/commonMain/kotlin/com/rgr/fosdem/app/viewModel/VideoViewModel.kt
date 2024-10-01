package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import com.rgr.fosdem.domain.model.bo.VideoBo
import com.rgr.fosdem.domain.useCase.GetVideoUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VideoViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val videoUseCase: GetVideoUseCase
): ViewModel() {

    private val _state = MutableStateFlow(VideoState())
    val state = _state.asStateFlow()

    init {
        getVideos()
    }

    fun getVideos() {
        _state.update { it.copy(isLoading = true) }
        val videos = videoUseCase.invoke()
        videos.getOrNull()?.let { videoList ->
            _state.update { it.copy(isLoading = false, videos = videoList) }
        } ?: handleError()
    }

    private fun handleError() {

    }
}

data class VideoState(
    val isLoading: Boolean = true,
    val videos: List<VideoBo> = emptyList()
)