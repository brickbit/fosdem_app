package com.rgr.fosdem.app.viewModel

import com.rgr.fosdem.app.flow.toCommonStateFlow
import com.rgr.fosdem.app.state.FavouriteEventsState
import com.rgr.fosdem.app.state.MainPreferredTracksState
import com.rgr.fosdem.app.state.MainTracksNowState
import com.rgr.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByTrackUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListEventsViewModel(
    private val getFavouritesEvents: GetFavouritesEventsUseCase,
    private val getScheduleByHour: GetScheduleByHourUseCase,
    private val getPreferredTracks: GetPreferredTracksUseCase,
    private val getScheduleByTrack: GetScheduleByTrackUseCase,

    ): BaseViewModel() {
    private val _stateFavouriteEvents: MutableStateFlow<FavouriteEventsState> = MutableStateFlow(
        FavouriteEventsState.Loading)
    val stateFavouriteEvents = _stateFavouriteEvents.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FavouriteEventsState.Loading
    ).toCommonStateFlow()
    private val _stateCurrentTracks: MutableStateFlow<MainTracksNowState> = MutableStateFlow(
        MainTracksNowState.Loading)
    val stateCurrentTracks = _stateCurrentTracks.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainTracksNowState.Loading
    ).toCommonStateFlow()
    private val _statePreferredTracks: MutableStateFlow<MainPreferredTracksState> = MutableStateFlow(MainPreferredTracksState.Loading)
    val statePreferredTracks = _statePreferredTracks.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainPreferredTracksState.Loading
    ).toCommonStateFlow()

    fun getFavouritesEvents() {
        scope.launch {
            getFavouritesEvents.invoke()
                .onSuccess { events ->
                    _stateFavouriteEvents.update {
                        FavouriteEventsState.Loaded(events)
                    }
                }
                .onFailure {  }
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
}

sealed class EventType {
    data object FavoriteEvents: EventType()
    data class FavoriteTracks(val trackId: String): EventType()
    data object CurrentEvents: EventType()
}