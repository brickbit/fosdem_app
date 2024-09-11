package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.state.FavouriteEventsState
import com.rgr.fosdem.app.state.MainPreferredTracksState
import com.rgr.fosdem.app.state.MainTracksNowState
import com.rgr.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByTrackUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class ListEventsViewModel(
    private val getFavouritesEvents: GetFavouritesEventsUseCase,
    private val getScheduleByHour: GetScheduleByHourUseCase,
    private val getPreferredTracks: GetPreferredTracksUseCase,
    private val getScheduleByTrack: GetScheduleByTrackUseCase,
): ViewModel() {

    private val _stateFavouriteEvents: MutableStateFlow<FavouriteEventsState> = MutableStateFlow(
        FavouriteEventsState.Loading)
    val stateFavouriteEvents = _stateFavouriteEvents.asStateFlow()

    private val _stateCurrentTracks: MutableStateFlow<MainTracksNowState> = MutableStateFlow(
        MainTracksNowState.Loading)
    val stateCurrentTracks = _stateCurrentTracks.asStateFlow()

    private val _statePreferredTracks: MutableStateFlow<MainPreferredTracksState> = MutableStateFlow(MainPreferredTracksState.Loading)
    val statePreferredTracks = _statePreferredTracks.asStateFlow()

    fun getFavouritesEvents() {
        viewModelScope.launch {
            getFavouritesEvents.invoke()
                .onSuccess { events ->
                    if(events.isEmpty()) {
                        _stateFavouriteEvents.update {
                            FavouriteEventsState.Empty
                        }
                    } else {
                        _stateFavouriteEvents.update {
                            FavouriteEventsState.Loaded(events)
                        }
                    }
                }
                .onFailure {  }
        }
    }

    fun getScheduleByMoment(instant: Instant = Clock.System.now()) {
        viewModelScope.launch {
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

                }
        }
    }

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
}

sealed class EventType {
    data object FavoriteEvents: EventType()
    data class FavoriteTracks(val trackId: String): EventType()
    data object CurrentEvents: EventType()
}