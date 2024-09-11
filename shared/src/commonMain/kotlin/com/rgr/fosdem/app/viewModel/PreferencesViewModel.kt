package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.state.PreferencesState
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.useCase.GetSavedTracksUseCase
import com.rgr.fosdem.domain.useCase.GetTracksUseCase
import com.rgr.fosdem.domain.useCase.SaveFavouriteTracksShownUseCase
import com.rgr.fosdem.domain.useCase.SavePreferredTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val getTracks: GetTracksUseCase,
    private val saveTracks: SavePreferredTracksUseCase,
    private val checkShownTracks: SaveFavouriteTracksShownUseCase,
    private val getSavedTracks: GetSavedTracksUseCase
): ViewModel() {

    private val _state: MutableStateFlow<PreferencesState> = MutableStateFlow(PreferencesState.Loading)
    val state = _state.asStateFlow()

    fun getPreferences() {
        viewModelScope.launch {
            getTracks.invoke()
                .onSuccess { tracks ->
                    val savedTracks = getSavedTracks.invoke()
                    val checkedTracks = tracks.map { track ->
                        track.copy(checked = savedTracks.count { track.name == it.name } > 0)
                    }
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
        viewModelScope.launch {
            checkShownTracks.invoke()
            saveTracks.invoke(tracks.filter { it.checked })
            _state.update {
                PreferencesState.Saved
            }
        }
    }

    fun skipFavouriteTracks() {
        viewModelScope.launch {
            checkShownTracks.invoke()
        }
    }

    fun enableContinueButton(tracks: List<TrackBo>): Boolean {
        return tracks.count { it.checked } > 0
    }

}