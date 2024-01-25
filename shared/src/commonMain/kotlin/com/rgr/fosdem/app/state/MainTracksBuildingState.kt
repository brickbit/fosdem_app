package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.EventBo

sealed class MainTracksBuildingState {
    data object Loading: MainTracksBuildingState()
    data class Loaded(val events: List<EventBo>): MainTracksBuildingState()
}