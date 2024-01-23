package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.EventBo

sealed class MainTracksBuildingState {
    data object Loading: MainTracksBuildingState()
    data class Loaded(val events: List<EventBo>): MainTracksBuildingState()
}