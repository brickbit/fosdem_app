package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _state = MutableStateFlow(PreferencesState())
    val state = _state.asStateFlow()

    fun getPreferences() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getTracks.invoke()
                .onSuccess { tracks ->
                    val savedTracks = getSavedTracks.invoke()
                    val checkedTracks = tracks.map { track ->
                        track.copy(checked = savedTracks.count { track.name == it.name } > 0)
                    }
                    _state.update { it.copy(isLoading = false, tracks = checkedTracks) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, tracks = emptyList(), isError = true) }
                }
        }
    }

    fun onTrackChecked(track: TrackBo, checked: Boolean) {
        val newTrackList = state.value.tracks.map {
            if(it == track) {
                TrackBo(id = track.id, name = track.name, events = track.events, checked = checked, stands = track.stands)
            } else {
                it
            }
        }
        _state.update { it.copy(tracks = newTrackList) }
    }

    fun savePreferredTracks(tracks: List<TrackBo>) {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            checkShownTracks.invoke()
            saveTracks.invoke(tracks.filter { it.checked })
            _state.update { it.copy(isSaved = true) }

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

data class PreferencesState (
    val isLoading: Boolean = false,
    val tracks: List<TrackBo> = emptyList(),
    val isSaved: Boolean = false,
    val isError: Boolean = false
)