package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.EventBo

sealed class MainTracksNowState {
    data object Loading: MainTracksNowState()
    data class Loaded(val events: List<EventBo>): MainTracksNowState()
    data object Empty: MainTracksNowState()
    data object Error: MainTracksNowState()

}