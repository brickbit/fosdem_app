package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.TrackBo

sealed class MainState {
    data object Loading: MainState()
    data class Loaded(val tracks: List<TrackBo>): MainState()
}