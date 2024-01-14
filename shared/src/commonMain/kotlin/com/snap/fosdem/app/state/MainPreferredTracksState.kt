package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.TrackBo

sealed class MainPreferredTracksState {
    data object Loading: MainPreferredTracksState()
    data class Loaded(val tracks: List<TrackBo>): MainPreferredTracksState()
}