package com.rgr.fosdem.android.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rgr.fosdem.android.screens.common.EventItem
import com.rgr.fosdem.android.screens.common.TitleTopBar
import com.rgr.fosdem.app.state.FavouriteEventsState
import com.rgr.fosdem.app.state.MainPreferredTracksState
import com.rgr.fosdem.app.state.MainTracksNowState
import com.rgr.fosdem.app.viewModel.EventType
import com.rgr.fosdem.app.viewModel.ListEventsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListEventsRoute(
    viewModel: ListEventsViewModel = koinViewModel(),
    title: String,
    eventType: EventType,
    onEventClicked: (String) -> Unit
) {
    val favouriteEventsState = viewModel.stateFavouriteEvents.collectAsState().value
    val preferredTracksState = viewModel.statePreferredTracks.collectAsState().value
    val tracksNowState = viewModel.stateCurrentTracks.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getScheduleByMoment()
        viewModel.getPreferredTracks()
        viewModel.getFavouritesEvents()
    }

    ListEventScreen(
        title = title,
        eventType = eventType,
        favourites = favouriteEventsState,
        tracksNow = tracksNowState,
        preferredTracks = preferredTracksState,
        onEventClicked = onEventClicked
    )

}
@Composable
fun ListEventScreen(
    title: String,
    eventType: EventType,
    favourites: FavouriteEventsState,
    tracksNow: MainTracksNowState,
    preferredTracks: MainPreferredTracksState,
    onEventClicked: (String) -> Unit
) {
    LazyColumn {
        item {
            TitleTopBar(title = title)
        }
        when(eventType) {
            EventType.CurrentEvents -> {
                if(tracksNow is MainTracksNowState.Loaded) {
                    items(tracksNow.events) {
                        EventItem(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            event = it,
                            favourites = favourites,
                            onClickAction = onEventClicked
                        )
                    }
                }
            }
            EventType.FavoriteEvents -> {
                if(favourites is FavouriteEventsState.Loaded) {
                    items(favourites.events) {
                        EventItem(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            event = it,
                            favourites = favourites,
                            onClickAction = onEventClicked
                        )
                    }
                }
            }
            is EventType.FavoriteTracks -> {
                if(preferredTracks is MainPreferredTracksState.Loaded) {
                    val selectedTrack = preferredTracks.tracks.first { it.id == eventType.trackId }
                    items(selectedTrack.events) {
                        EventItem(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            event = it,
                            favourites = favourites,
                            onClickAction = onEventClicked
                        )
                    }
                }
            }
        }
    }
}