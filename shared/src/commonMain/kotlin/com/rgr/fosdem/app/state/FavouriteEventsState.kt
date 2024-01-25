package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.EventBo

sealed class FavouriteEventsState {
    data object Loading: FavouriteEventsState()
    data class Loaded(val events: List<EventBo>): FavouriteEventsState()
    data object Empty: FavouriteEventsState()

}