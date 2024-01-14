package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.MainPreferredTracksState
import com.snap.fosdem.app.state.MainTracksBuildingState
import com.snap.fosdem.app.state.MainTracksNowState
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByBuildingUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByTrackUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getPreferredTracks: GetPreferredTracksUseCase,
    private val getScheduleByTrack: GetScheduleByTrackUseCase,
    private val getScheduleByHour: GetScheduleByHourUseCase,
    private val getSchedulesByBuilding: GetScheduleByBuildingUseCase
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
    private val _stateBuildingTracks: MutableStateFlow<MainTracksBuildingState> = MutableStateFlow(MainTracksBuildingState.Loading)
    val stateBuildingTracks = _stateBuildingTracks.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainTracksBuildingState.Loading
    ).toCommonStateFlow()

    fun getPreferredTracks() {
        scope.launch {
            val tracks = getPreferredTracks.invoke()
            val schedules = tracks.mapNotNull { track ->
                getScheduleByTrack.invoke(track.name).getOrNull()
            }
            _statePreferredTracks.update {
                MainPreferredTracksState.Loaded(schedules)
            }
        }
    }

    fun getScheduleByMoment() {
        scope.launch {
            getScheduleByHour.invoke("10:30")
                .onSuccess { events ->
                    _stateCurrentTracks.update {
                        MainTracksNowState.Loaded(events)
                    }
                }
                .onFailure {

                }
        }
    }

    fun getScheduleByBuilding() {
        scope.launch {
            getSchedulesByBuilding.invoke("UB2.252A (Lameere)")
                .onSuccess { events ->
                    _stateBuildingTracks.update {
                        MainTracksBuildingState.Loaded(events)
                    }
                }
                .onFailure {  }
        }
    }
}