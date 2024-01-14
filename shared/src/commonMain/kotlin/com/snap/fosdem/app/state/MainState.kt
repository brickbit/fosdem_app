package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.TrackBo

sealed class MainState {
    data object Loading: MainState()
    data class Loaded(val tracks: List<TrackBo>): MainState()
}