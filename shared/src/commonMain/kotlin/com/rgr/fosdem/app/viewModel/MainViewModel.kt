package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.state.MainPreferredTracksState
import com.rgr.fosdem.app.state.MainTracksNowState
import com.rgr.fosdem.app.state.SpeakersState
import com.rgr.fosdem.app.state.StandsState
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByTrackUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleDataUseCase
import com.rgr.fosdem.domain.useCase.GetSpeakersUseCase
import com.rgr.fosdem.domain.useCase.GetStandsUseCase
import com.rgr.fosdem.domain.useCase.IsUpdateNeeded
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MainViewModel(
    private val getSchedule: GetScheduleDataUseCase,
    private val getPreferredTracks: GetPreferredTracksUseCase,
    private val getScheduleByTrack: GetScheduleByTrackUseCase,
    private val getScheduleByHour: GetScheduleByHourUseCase,
    private val getFavouritesEvents: GetFavouritesEventsUseCase,
    private val getSpeakers: GetSpeakersUseCase,
    private val getStands: GetStandsUseCase,
    private val needUpdate: IsUpdateNeeded,
): ViewModel() {

    private val _statePreferredTracks: MutableStateFlow<MainPreferredTracksState> = MutableStateFlow(MainPreferredTracksState.Loading)
    val statePreferredTracks = _statePreferredTracks.asStateFlow()

    private val _stateCurrentTracks: MutableStateFlow<MainTracksNowState> = MutableStateFlow(MainTracksNowState.Loading)
    val stateCurrentTracks = _stateCurrentTracks.asStateFlow()

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _stateSpeaker: MutableStateFlow<SpeakersState> = MutableStateFlow(SpeakersState.Loading)
    val stateSpeaker = _stateSpeaker.asStateFlow()

    private val _stateStand: MutableStateFlow<StandsState> = MutableStateFlow(StandsState.Loading)
    val stateStand = _stateStand.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getPreferredTracks() {
        viewModelScope.launch {
            val tracks = getPreferredTracks.invoke()
            val schedules = tracks.mapNotNull { track ->
                getScheduleByTrack.invoke(track.name).getOrNull()
            }
            if(schedules.isEmpty()) {
                _statePreferredTracks.update {
                    MainPreferredTracksState.Empty
                }
            } else {
                _statePreferredTracks.update {
                    MainPreferredTracksState.Loaded(schedules)
                }
            }
        }
    }

    fun getScheduleByMoment(instant: Instant = Clock.System.now()) {
        viewModelScope.launch {
            _stateCurrentTracks.update {
                MainTracksNowState.Loading
            }
            getScheduleByHour.invoke(instant)
                .onSuccess { events ->
                    _stateCurrentTracks.update {
                        if (events.isEmpty()) {
                            MainTracksNowState.Empty
                        } else {
                            MainTracksNowState.Loaded(events)
                        }
                    }
                }
                .onFailure {
                    _stateCurrentTracks.update {
                        MainTracksNowState.Error
                    }
                }
        }
    }

    fun getFavouritesEvents() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            getFavouritesEvents.invoke()
                .onSuccess { events ->
                    _state.update {
                        it.copy(isLoading = false, favouriteEvents = events)
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(isLoading = false, favouriteEvents = emptyList())
                    }
                }
        }
    }

    fun getSpeakerList() {
        viewModelScope.launch {
            _stateSpeaker.update {
                SpeakersState.Loading
            }
            getSpeakers.invoke()
                .onSuccess { speakers ->
                    if(speakers.isEmpty()) {
                        _stateSpeaker.update {
                            SpeakersState.Empty
                        }
                    } else {
                        _stateSpeaker.update {
                            SpeakersState.Loaded(speakers)
                        }
                    }
                }
                .onFailure {  }
        }
    }

    fun getStandList() {
        viewModelScope.launch {
            getStands.invoke()
                .onSuccess { stands ->
                    if(stands.isEmpty()) {
                        _stateStand.update {
                            StandsState.Empty
                        }
                    } else {
                        _stateStand.update {
                            StandsState.Loaded(stands)
                        }
                    }
                }
                .onFailure {  }
        }
    }

    fun onRefresh() {
        /*scope.launch {
            needUpdate.invoke()
                .onSuccess { shouldUpdate ->
                    if (shouldUpdate) {
                        getSchedule.invoke()
                            .onSuccess{
                                _isRefreshing.update { false }
                            }
                        getSpeakerList()
                        getStandList()
                    } else {
                    }
                }
                .onFailure {
                    _isRefreshing.update { false }
                }

        }*/
    }
}

data class MainState (
    val isLoading: Boolean = false,
    val favouriteEvents: List<EventBo> = emptyList()
)