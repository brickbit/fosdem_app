package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
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

    private val _stateTracks: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val stateTracks = _stateTracks.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    ).toCommonStateFlow()

    private val _stateRooms: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val stateRooms = _stateRooms.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
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
                    _stateTracks.update { tracks.map { it.name } }
                }
                .onFailure {
                }
        }
    }

    fun getRooms() {
        scope.launch {
            getRoomsUseCase.invoke()
                .onSuccess { rooms ->
                    _stateRooms.update { rooms }
                }
                .onFailure {
                }
        }
    }

    fun getScheduleBy(
        day: String,
        hours: List<String>,
        tracks: List<String>,
        rooms: List<String>,
    ) {
        scope.launch {
            getScheduleByParameter.invoke(
                day = day,
                hours = hours,
                tracks = tracks,
                rooms = rooms
            )
                .onSuccess { events ->
                    _state.update {
                        ScheduleState.Loaded(
                            day = day,
                            hours = hours,
                            tracks = tracks,
                            rooms = rooms,
                            events  = events,
                        )
                    }
                }
                .onFailure {

                }
        }
    }
}