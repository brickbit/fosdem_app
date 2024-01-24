package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.PreferencesState
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.useCase.GetSavedTracksUseCase
import com.snap.fosdem.domain.useCase.GetTracksUseCase
import com.snap.fosdem.domain.useCase.SaveFavouriteTracksShownUseCase
import com.snap.fosdem.domain.useCase.SavePreferredTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val getTracks: GetTracksUseCase,
    private val saveTracks: SavePreferredTracksUseCase,
    private val checkShownTracks: SaveFavouriteTracksShownUseCase,
    private val getSavedTracks: GetSavedTracksUseCase
): BaseViewModel() {

    private val _state: MutableStateFlow<PreferencesState> = MutableStateFlow(PreferencesState.Loading)
    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PreferencesState.Loading
    ).toCommonStateFlow()

    fun getPreferences() {
        scope.launch {
            getTracks.invoke()
                .onSuccess { tracks ->
                    val savedTracks = getSavedTracks.invoke()
                    val checkedTracks = tracks.map { track ->
                        track.copy(checked = savedTracks.count { track.name == it.name } > 0) }
                    _state.update {
                        PreferencesState.Loaded(checkedTracks)
                    }
                }
                .onFailure {
                    _state.update {
                        PreferencesState.Error
                    }
                }
        }
    }

    fun onTrackChecked(track: TrackBo, checked: Boolean) {
        val newTrackList = (state.value as PreferencesState.Loaded).tracks.map {
            if(it == track) {
                TrackBo(id = track.id, name = track.name, events = track.events, checked = checked, stands = track.stands)
            } else {
                it
            }
        }
        _state.update {
            PreferencesState.Loaded(newTrackList)
        }
    }

    fun savePreferredTracks(tracks: List<TrackBo>) {
        _state.update {
            PreferencesState.Loading
        }
        scope.launch {
            checkShownTracks.invoke()
            saveTracks.invoke(tracks.filter { it.checked })
            _state.update {
                PreferencesState.Saved
            }
        }
    }

    fun skipFavouriteTracks() {
        scope.launch {
            checkShownTracks.invoke()
        }
    }

    fun enableContinueButton(tracks: List<TrackBo>): Boolean {
        return tracks.count { it.checked } > 0
    }

}