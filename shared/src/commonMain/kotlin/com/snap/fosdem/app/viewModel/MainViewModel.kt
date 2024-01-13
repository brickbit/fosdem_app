package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.MainState
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getPreferredTracks: GetPreferredTracksUseCase
): BaseViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.Loading)
    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainState.Loading
    ).toCommonStateFlow()

    fun getPreferredTracks() {
        scope.launch {
            val tracks = getPreferredTracks.invoke()
            _state.update {
                MainState.Loaded(tracks)
            }
        }
    }
}