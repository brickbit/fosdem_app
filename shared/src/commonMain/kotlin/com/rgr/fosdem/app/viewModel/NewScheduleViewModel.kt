package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.useCase.GetDaysUseCase
import com.rgr.fosdem.domain.useCase.GetNewHoursUseCase
import com.rgr.fosdem.domain.useCase.GetNewRoomsUseCase
import com.rgr.fosdem.domain.useCase.GetNewTracksUseCase
import com.rgr.fosdem.domain.useCase.GetSchedulesUseCase
import com.rgr.fosdem.domain.useCase.SetFavouriteUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewScheduleViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val scheduleUseCase: GetSchedulesUseCase,
    private val daysUseCase: GetDaysUseCase,
    private val tracksUseCase: GetNewTracksUseCase,
    private val roomsUseCase: GetNewRoomsUseCase,
    private val hoursUseCase: GetNewHoursUseCase,
    private val setFavouriteUseCase: SetFavouriteUseCase
): ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    val state = _state.asStateFlow()

    init {
        getSchedules()
        getDays()
        getHours()
        getTracks()
        getRooms()
    }

    fun getSchedules() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val schedules = scheduleUseCase.invoke(
                date = state.value.selectedDay,
                start = state.value.selectedHours,
                title = state.value.selectedTitle,
                track = state.value.selectedTrack,
                room = state.value.selectedRoom,
                speaker = state.value.selectedSpeaker
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
    }

    fun filterByTitleOrSpeaker(title: String, speaker: String) {
        _state.update { it.copy(selectedTitle = title, selectedSpeaker = speaker) }
        getSchedules()
    }

    fun filter(
        selectedDay: String = "",
        selectedTrack: String = "",
        selectedHours: List<String> = emptyList(),
        selectedRoom: String
    ) {
        _state.update {
            it.copy(
                selectedDay = selectedDay,
                selectedTrack = selectedTrack,
                selectedHours = selectedHours,
                selectedRoom = selectedRoom
            )
        }
        getSchedules()
    }

    private fun getDays() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = daysUseCase.invoke()
            result.getOrNull()?.let { days ->
                _state.update { it.copy(isLoading = false, days = days) }
            } ?: handleError()
        }
    }

    private fun getTracks() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = tracksUseCase.invoke()
            result.getOrNull()?.let { tracks ->
                _state.update { it.copy(isLoading = false, tracks = tracks) }
            } ?: handleError()
        }
    }

    private fun getRooms() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = roomsUseCase.invoke()
            result.getOrNull()?.let { rooms ->
                _state.update { it.copy(isLoading = false, rooms = rooms) }
            } ?: handleError()
        }
    }

    private fun getHours() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = hoursUseCase.invoke()
            result.getOrNull()?.let { hours ->
                _state.update { it.copy(isLoading = false, hours = hours) }
            } ?: handleError()
        }
    }

    private fun handleError() {
        _state.update {
            it.copy(
                isLoading = false,
                schedules = emptyList()
            )
        }
    }

    fun notifyEvent(schedule: ScheduleBo) {
        setFavouriteUseCase.invoke(
            schedule = schedule,
            list = state.value.schedules,
            favourite = true
        )
        getSchedules()
    }

    fun notNotifyEvent(schedule: ScheduleBo) {
        setFavouriteUseCase.invoke(
            schedule = schedule,
            list = state.value.schedules,
            favourite = false
        )
        getSchedules()
    }
}

data class ScheduleState(
    val isLoading: Boolean = true,
    val schedules: List<ScheduleBo> = emptyList(),
    val days: List<String> = emptyList(),
    val tracks: List<String> = emptyList(),
    val hours: List<String> = emptyList(),
    val rooms: List<String> = emptyList(),
    val selectedTitle: String = "",
    val selectedDay: String = "",
    val selectedTrack: String = "",
    val selectedHours: List<String> = emptyList(),
    val selectedSpeaker: String = "",
    val selectedRoom: String = ""
)