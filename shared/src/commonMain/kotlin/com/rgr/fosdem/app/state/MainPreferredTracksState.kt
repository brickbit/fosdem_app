package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.TrackBo

sealed class MainPreferredTracksState {
    data object Loading: MainPreferredTracksState()
    data class Loaded(val tracks: List<TrackBo>): MainPreferredTracksState()
    data object Empty: MainPreferredTracksState()
}