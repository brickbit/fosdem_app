package com.rgr.fosdem.android.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rgr.fosdem.android.screens.common.EventItem
import com.rgr.fosdem.android.screens.common.TitleTopBar
import com.rgr.fosdem.app.viewModel.EventType
import com.rgr.fosdem.app.viewModel.ListEventsViewModel
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListEventsRoute(
    viewModel: ListEventsViewModel = koinViewModel(),
    title: String,
    eventType: EventType,
    onEventClicked: (String) -> Unit
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getScheduleByMoment()
        viewModel.getPreferredTracks()
        viewModel.getFavouritesEvents()
    }

    ListEventScreen(
        title = title,
        eventType = eventType,
        favourites = state.favouriteEvents,
        tracksNow = state.tracksNow,
        preferredTracks = state.tracks,
        onEventClicked = onEventClicked
    )

}
@Composable
fun ListEventScreen(
    title: String,
    eventType: EventType,
    favourites: List<ScheduleBo>,
    tracksNow: List<ScheduleBo>,
    preferredTracks: List<TrackBo>,
    onEventClicked: (String) -> Unit
) {
    LazyColumn {
        item {
            TitleTopBar(title = title)
        }
        when(eventType) {
            EventType.CurrentEvents -> {
                items(tracksNow) {
                    EventItem(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        event = it,
                        favourites = favourites,
                        onClickAction = onEventClicked
                    )
                }
            }
            EventType.FavoriteEvents -> {
                items(favourites) {
                    EventItem(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        event = it,
                        favourites = favourites,
                        onClickAction = onEventClicked
                    )
                }
            }
            is EventType.FavoriteTracks -> {
                    val selectedTrack = preferredTracks.first { it.id == eventType.trackId }
                    items(selectedTrack.events) {
                        /*EventItem(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            event = it,
                            favourites = favourites,
                            onClickAction = onEventClicked
                        )*/
                    }

            }
        }
    }
}