package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.rgr.fosdem.domain.useCase.GetHoursUseCase
import com.rgr.fosdem.domain.useCase.GetRoomsUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByParameterUseCase
import com.rgr.fosdem.domain.useCase.GetTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val getHoursUseCase: GetHoursUseCase,
    private val getTracksUseCase: GetTracksUseCase,
    private val getRoomsUseCase: GetRoomsUseCase,
    private val getScheduleByParameter: GetScheduleByParameterUseCase,
    private val getFavouritesEvents: GetFavouritesEventsUseCase
): ViewModel() {

    private val _stateHour: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val stateHour = _stateHour.asStateFlow()

    private val _stateTracks: MutableStateFlow<List<String>> = MutableStateFlow(listOf(""))
    val stateTracks = _stateTracks.asStateFlow()

    private val _stateRooms: MutableStateFlow<List<String>> = MutableStateFlow(listOf(""))
    val stateRooms = _stateRooms.asStateFlow()

    private val _newState = MutableStateFlow(NewScheduleState())
    val newState = _newState.asStateFlow()

    fun getHours(day: String) {
        viewModelScope.launch {
            getHoursUseCase.invoke(day)
                .onSuccess { hours ->
                    _stateHour.update {
                        hours
                    }
                }
                .onFailure {
                    print("")
                }
        }
    }

    fun getTracks() {
        viewModelScope.launch {
            getTracksUseCase.invoke()
                .onSuccess { tracks ->
                    val list = tracks.map { it.name }.toMutableList()
                    list.add(0, "All")
                    _stateTracks.update { list.toList() }
                }
                .onFailure {
                }
        }
    }

    fun getRooms(track: String) {
        viewModelScope.launch {
            getRoomsUseCase.invoke(track)
                .onSuccess { rooms ->
                    val list = rooms.map { it }.toMutableList()
                    list.add(0, "All")
                    _stateRooms.update { list.toList() }
                }
                .onFailure {
                }
        }
    }

    fun getScheduleBy(
        day: String,
        hours: List<String>,
        track: String,
        room: String,
    ) {
        viewModelScope.launch {
            _newState.update{ it.copy(isLoading = true) }
            getScheduleByParameter.invoke(
                day = day,
                hours = hours,
                track = track,
                room = room
            )
                .onSuccess { events ->
                    if(events.isNotEmpty()) {
                        _newState.update{
                            it.copy(
                                isLoading = true,
                                isEmpty = true,
                                filter = ScheduleFilter(
                                    day = day,
                                    hours = hours,
                                    track = track,
                                    room = room,
                                    events = events
                                )
                            )
                        }
                    } else {
                        _newState.update{
                            it.copy(
                                isLoading = true,
                                isEmpty = false,
                                filter = ScheduleFilter(
                                    day = day,
                                    hours = hours,
                                    track = track,
                                    room = room,
                                    events = events
                                )
                            )
                        }
                    }
                }
                .onFailure {

                }
        }
    }

    fun getFavouritesEvents() {
        _newState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getFavouritesEvents.invoke()
                .onSuccess { events ->
                    _newState.update { it.copy(isLoading = false, favouriteEvents = events) }
                }
                .onFailure {
                    _newState.update { it.copy(isLoading = false, favouriteEvents = emptyList()) }
                }
        }
    }

    fun removeSelectedHour(hour: String) {
        viewModelScope.launch {
            removeHour(hour,newState.value.filter)
        }
    }

    private fun removeHour(hour: String, filter: ScheduleFilter) {
        val newHours = filter.hours.toMutableList()
        newHours.remove(hour)
        getScheduleBy(
            day = filter.day,
            hours = newHours,
            track = filter.track,
            room = filter.room
        )
    }

    fun addSelectedHour(hour: String) {
        viewModelScope.launch {
            addHour(hour,newState.value.filter)
        }
    }

    private fun addHour(hour: String, filter: ScheduleFilter) {
        val newHours = filter.hours.toMutableList()
        newHours.add(hour)
        getScheduleBy(
            day = filter.day,
            hours = newHours,
            track = filter.track,
            room = filter.room
        )
    }
}

data class NewScheduleState(
    val isLoading: Boolean = false,
    val favouriteEvents: List<EventBo> = emptyList(),
    val isEmpty: Boolean = true,
    val filter: ScheduleFilter = ScheduleFilter()
)

data class ScheduleFilter(
    val day: String = "Saturday",
    val hours: List<String> = emptyList(),
    val room: String = "",
    val track: String = "",
    val events: List<EventBo> = emptyList(),
)