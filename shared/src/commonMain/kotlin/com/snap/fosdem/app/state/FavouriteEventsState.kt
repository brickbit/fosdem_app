package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.EventBo

sealed class FavouriteEventsState {
    data object Loading: FavouriteEventsState()
    data class Loaded(val events: List<EventBo>): FavouriteEventsState()
    data object Empty: FavouriteEventsState()

}