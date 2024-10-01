package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.useCase.GetSchedulesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewScheduleViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val scheduleUseCase: GetSchedulesUseCase
): ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    val state = _state.asStateFlow()

    init {
        getSchedules()
    }

    fun getSchedules(
        date: String = "",
        start: String = "",
        duration: String = "",
        title: String = "",
        track: String = "",
        type: String = "",
        speaker: String = ""
    ) {
        _state.update { it.copy(isLoading = true) }
        val schedules = scheduleUseCase.invoke(
            date = date,
            start = start,
            duration = duration,
            title = title,
            track = track,
            type = type,
            speaker = speaker
        )
        schedules.getOrNull()?.let { savedSchedules ->
            _state.update {
                it.copy(
                    isLoading = false,
                    schedules = savedSchedules
                )
            }
        } ?: handleError()
    }

    private fun handleError() {
        _state.update {
            it.copy(
                isLoading = false,
                schedules = emptyList()
            )
        }
    }
}

data class ScheduleState(
    val isLoading: Boolean = true,
    val schedules: List<ScheduleBo> = emptyList()
)