package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.TrackBo

sealed class PreferencesState {
    data object Loading: PreferencesState()
    data class Loaded(val tracks: List<TrackBo>): PreferencesState()
    data object Saved: PreferencesState()
    data object Error: PreferencesState()
}