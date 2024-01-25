package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.TrackBo

sealed class PreferencesState {
    data object Loading: PreferencesState()
    data class Loaded(val tracks: List<TrackBo>): PreferencesState()
    data object Saved: PreferencesState()
    data object Error: PreferencesState()
}