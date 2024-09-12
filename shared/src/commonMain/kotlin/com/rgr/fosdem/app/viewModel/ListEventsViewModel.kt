package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.TrackBo
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

    private val _state = MutableStateFlow(ListEventsState())
    val state = _state.asStateFlow()

    fun getFavouritesEvents() {
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
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

    fun getScheduleByMoment(instant: Instant = Clock.System.now()) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getScheduleByHour.invoke(instant)
                .onSuccess { events ->
                    _state.update { it.copy(isLoading = false, tracksNow = events) }
                }
                .onFailure {

                }
        }
    }

    fun getPreferredTracks() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val tracks = getPreferredTracks.invoke()
            val schedules = tracks.mapNotNull { track ->
                getScheduleByTrack.invoke(track.name).getOrNull()
            }
            _state.update { it.copy(isLoading = false, tracks = schedules) }
        }
    }
}

sealed class EventType {
    data object FavoriteEvents: EventType()
    data class FavoriteTracks(val trackId: String): EventType()
    data object CurrentEvents: EventType()
}

data class ListEventsState(
    val isLoading: Boolean = false,
    val favouriteEvents: List<EventBo> = emptyList(),
    val tracks: List<TrackBo> = emptyList(),
    val tracksNow: List<EventBo> = emptyList()
)