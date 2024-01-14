package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.EventBo

sealed class MainTracksNowState {
    data object Loading: MainTracksNowState()
    data class Loaded(val events: List<EventBo>): MainTracksNowState()
}