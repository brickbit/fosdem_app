package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.PreferencesState
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.useCase.GetTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val getTracks: GetTracksUseCase
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
                    _state.update {
                        PreferencesState.Loaded(tracks)
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
                TrackBo(name = track.name, events = track.events, checked = checked)
            } else {
                it
            }
        }

        _state.update {
            PreferencesState.Loaded(newTrackList)
        }
    }


}