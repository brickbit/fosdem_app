package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.domain.model.bo.VideoBo
import com.rgr.fosdem.domain.useCase.GetVideoUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            val videos = videoUseCase.invoke()
            videos.getOrNull()?.let { videoList ->
                val itemsByType = videoList.groupBy {
                    it.type
                }.toList()
                _state.update { it.copy(isLoading = false, videosForType = itemsByType, videos = videoList) }
            } ?: handleError()
        }

    }

    private fun handleError() {

    }
}

data class VideoState(
    val isLoading: Boolean = true,
    val videos: List<VideoBo> = emptyList(),
    val videosForType: List<Pair<String, List<VideoBo>>> = emptyList()
)