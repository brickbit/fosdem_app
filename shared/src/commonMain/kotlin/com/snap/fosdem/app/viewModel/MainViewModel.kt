package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.FavouriteEventsState
import com.snap.fosdem.app.state.MainPreferredTracksState
import com.snap.fosdem.app.state.MainTracksNowState
import com.snap.fosdem.app.state.SpeakersState
import com.snap.fosdem.app.state.StandsState
import com.snap.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByTrackUseCase
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import com.snap.fosdem.domain.useCase.GetSpeakersUseCase
import com.snap.fosdem.domain.useCase.GetStandsUseCase
import com.snap.fosdem.domain.useCase.IsUpdateNeeded
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class MainViewModel(
    private val getSchedule: GetScheduleDataUseCase,
    private val getPreferredTracks: GetPreferredTracksUseCase,
    private val getScheduleByTrack: GetScheduleByTrackUseCase,
    private val getScheduleByHour: GetScheduleByHourUseCase,
    private val getFavouritesEvents: GetFavouritesEventsUseCase,
    private val getSpeakers: GetSpeakersUseCase,
    private val getStands: GetStandsUseCase,
    private val needUpdate: IsUpdateNeeded,
): BaseViewModel() {

    private val _statePreferredTracks: MutableStateFlow<MainPreferredTracksState> = MutableStateFlow(MainPreferredTracksState.Loading)
    val statePreferredTracks = _statePreferredTracks.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainPreferredTracksState.Loading
    ).toCommonStateFlow()
    private val _stateCurrentTracks: MutableStateFlow<MainTracksNowState> = MutableStateFlow(MainTracksNowState.Loading)
    val stateCurrentTracks = _stateCurrentTracks.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainTracksNowState.Loading
    ).toCommonStateFlow()

    private val _stateFavouriteEvents: MutableStateFlow<FavouriteEventsState> = MutableStateFlow(FavouriteEventsState.Loading)
    val stateFavouriteEvents = _stateFavouriteEvents.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FavouriteEventsState.Loading
    ).toCommonStateFlow()

    private val _stateSpeaker: MutableStateFlow<SpeakersState> = MutableStateFlow(SpeakersState.Loading)
    val stateSpeaker = _stateSpeaker.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SpeakersState.Loading
    ).toCommonStateFlow()

    private val _stateStand: MutableStateFlow<StandsState> = MutableStateFlow(StandsState.Loading)
    val stateStand = _stateStand.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StandsState.Loading
    ).toCommonStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    ).toCommonStateFlow()

    fun getPreferredTracks() {
        scope.launch {
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

    fun getScheduleByMoment() {
        scope.launch {
            getScheduleByHour.invoke()
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

                }
        }
    }

    fun getFavouritesEvents() {
        scope.launch {
            getFavouritesEvents.invoke()
                .onSuccess { events ->
                    _stateFavouriteEvents.update {
                        if (events.isEmpty()) {
                            FavouriteEventsState.Empty
                        } else {
                            FavouriteEventsState.Loaded(events)
                        }
                    }
                }
                .onFailure {  }
        }
    }

    fun getSpeakerList() {
        scope.launch {
            getSpeakers.invoke()
                .onSuccess { speakers ->
                    _stateSpeaker.update {
                        SpeakersState.Loaded(speakers)
                    }
                }
                .onFailure {  }
        }
    }

    fun getStandList() {
        scope.launch {
            getStands.invoke()
                .onSuccess { stands ->
                    _stateStand.update {
                        StandsState.Loaded(stands)
                    }
                }
                .onFailure {  }
        }
    }

    fun onRefresh() {
        scope.launch {
            needUpdate.invoke()
                .onSuccess { shouldUpdate ->
                    if (shouldUpdate) {
                        getSchedule.invoke(shouldUpdate)
                            .onSuccess{
                                _isRefreshing.update { false }
                            }
                        getSpeakerList()
                        getStandList()
                    } else {
                        _isRefreshing.update { false }
                    }
                }
                .onFailure {
                    _isRefreshing.update { false }
                }

        }
    }
}