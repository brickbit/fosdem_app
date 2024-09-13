package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.TrackBo
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

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

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

    fun getScheduleByMoment(instant: Instant = Clock.System.now()) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getScheduleByHour.invoke(instant)
                .onSuccess { events ->
                    _state.update { it.copy(isLoading = false, tracksNow = events) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, tracksNow = emptyList()) }
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
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getSpeakers.invoke()
                .onSuccess { speakers ->
                    _state.update { it.copy(isLoading = false, speakers = speakers) }
                }
                .onFailure {  }
        }
    }

    fun getStandList() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getStands.invoke()
                .onSuccess { stands ->
                    _state.update { it.copy(isLoading = false, stands = stands) }
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
    val favouriteEvents: List<EventBo> = emptyList(),
    val tracks: List<TrackBo> = emptyList(),
    val tracksNow: List<EventBo> = emptyList(),
    val speakers: List<SpeakerBo> = emptyList(),
    val stands: List<StandBo> = emptyList()
)


