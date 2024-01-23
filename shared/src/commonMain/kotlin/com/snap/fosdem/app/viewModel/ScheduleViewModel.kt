package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.ScheduleFilter
import com.snap.fosdem.app.state.ScheduleState
import com.snap.fosdem.domain.useCase.GetHoursUseCase
import com.snap.fosdem.domain.useCase.GetRoomsUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByParameterUseCase
import com.snap.fosdem.domain.useCase.GetTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val getHoursUseCase: GetHoursUseCase,
    private val getTracksUseCase: GetTracksUseCase,
    private val getRoomsUseCase: GetRoomsUseCase,
    private val getScheduleByParameter: GetScheduleByParameterUseCase
): BaseViewModel() {

    private val _stateHour: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val stateHour = _stateHour.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    ).toCommonStateFlow()

    private val _stateTracks: MutableStateFlow<List<String>> = MutableStateFlow(listOf(""))
    val stateTracks = _stateTracks.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf("")
    ).toCommonStateFlow()

    private val _stateRooms: MutableStateFlow<List<String>> = MutableStateFlow(listOf(""))
    val stateRooms = _stateRooms.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf("")
    ).toCommonStateFlow()

    private val _state: MutableStateFlow<ScheduleState> = MutableStateFlow(ScheduleState.Loading)
    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScheduleState.Loading
    ).toCommonStateFlow()

    fun getHours() {
        scope.launch {
            getHoursUseCase.invoke()
                .onSuccess { hours ->
                    _stateHour.update {
                        hours
                    }
                }
                .onFailure {

                }
        }
    }

    fun getTracks() {
        scope.launch {
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

    fun getRooms() {
        scope.launch {
            getRoomsUseCase.invoke()
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
        scope.launch {
            _state.update{
                ScheduleState.Loading
            }
            getScheduleByParameter.invoke(
                day = day,
                hours = hours,
                track = track,
                room = room
            )
                .onSuccess { events ->
                    _state.update {
                        if(events.isNotEmpty()) {
                            ScheduleState.Loaded(
                                filter = ScheduleFilter(
                                    day = day,
                                    hours = hours,
                                    track = track,
                                    room = room,
                                    events = events
                                )
                            )
                        } else {
                            ScheduleState.Empty(
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
}