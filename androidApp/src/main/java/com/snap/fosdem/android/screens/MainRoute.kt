package com.snap.fosdem.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.snap.fosdem.android.MyApplicationTheme
import com.snap.fosdem.android.R
import com.snap.fosdem.android.extension.splitImage
import com.snap.fosdem.android.mainBrushColor
import com.snap.fosdem.android.screens.common.EventItem
import com.snap.fosdem.android.screens.common.shimmerEffect
import com.snap.fosdem.app.state.FavouriteEventsState
import com.snap.fosdem.app.state.MainPreferredTracksState
import com.snap.fosdem.app.state.MainTracksNowState
import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.TrackBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoute(
    viewModel: MainViewModel = koinViewModel(),
    onNavigate: (String) -> Unit,
    navigateToSchedule: () -> Unit,
) {
    val preferredTracksState = viewModel.statePreferredTracks.collectAsState().value
    val tracksNowState = viewModel.stateCurrentTracks.collectAsState().value
    val favouriteEventsState = viewModel.stateFavouriteEvents.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getScheduleByMoment()
        viewModel.getPreferredTracks()
        viewModel.getFavouritesEvents()
    }
    MainScreen(
        preferredTracks = preferredTracksState,
        tracksNow = tracksNowState,
        favourites = favouriteEventsState,
        onNavigate = onNavigate,
        navigateToSchedule = navigateToSchedule
    )
}

@Composable
fun MainScreen(
    preferredTracks: MainPreferredTracksState,
    tracksNow: MainTracksNowState,
    favourites: FavouriteEventsState,
    onNavigate: (String) -> Unit,
    navigateToSchedule: () -> Unit
) {
    LazyColumn {
        /*item {
            MainHeader()
        }*/
        item {
            ScheduleCard (navigateToSchedule = navigateToSchedule)
        }
        rightNowItems(
            tracksNow = tracksNow,
            favourites = favourites,
            onNavigate = onNavigate
        )
        preferredTracks(
            preferredTracks = preferredTracks,
            favourites = favourites,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun TrackRow(
    track: TrackBo,
    favourites: FavouriteEventsState,
    onNavigate: (String) -> Unit
) {
    Column {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp),
            text = track.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            items(track.events) { event ->
                EventItem(
                    modifier = Modifier
                        .fillParentMaxWidth(0.8f)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    favourites = favourites,
                    event = event,
                    onClickAction = onNavigate
                )
            }
        }
    }
}

@Composable
fun FooterEventItem(
    event: EventBo
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier
                .size(16.dp),
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Text(
            text = event.talk.room.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CardImageEvent(
    image: String?,
) {
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .background(Color.White, shape = CircleShape)
            .size(50.dp),
    ) {
        if (image != null) {
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                model = image,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            Image(
                modifier = Modifier
                    .size(50.dp),
                painter = painterResource(id = R.drawable.ic_account),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}

@Composable
fun MainHeader() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.fosdem_background),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                colorFilter = ColorFilter.tint(color = Color.White),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
            )
            Row {
                Text(
                    modifier = Modifier
                        .background(Color.Black)
                        .padding(8.dp),
                    text = stringResource(R.string.main_date),
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
                )
            }
        }
    }
}

@Composable
fun ScheduleCard(
    navigateToSchedule: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            )
            .background(
                brush = Brush.linearGradient(colorStops = mainBrushColor),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { navigateToSchedule() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ContextCompat.getDrawable(context,R.drawable.ic_launcher_foreground).splitImage()?.
            second?.asImageBitmap()?.let {
                Image(
                    modifier = Modifier.height(120.dp),
                    contentScale = ContentScale.FillHeight,
                    bitmap = it,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Text(
                modifier = Modifier
                    .width(150.dp)
                    .padding(vertical = 24.dp),
                text = stringResource(R.string.main_check_schedule),
                style = MaterialTheme.typography.titleSmall.copy(Color.White),
                textAlign = TextAlign.Center
            )
            ContextCompat.getDrawable(context,R.drawable.ic_launcher_foreground).splitImage()?.
            first?.asImageBitmap()?.let {
                Image(
                    modifier = Modifier.height(120.dp),
                    contentScale = ContentScale.FillHeight,
                    bitmap = it,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}

fun LazyListScope.rightNowItems(
    tracksNow: MainTracksNowState,
    favourites: FavouriteEventsState,
    onNavigate: (String) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.main_right_now),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    when(tracksNow) {
        is MainTracksNowState.Loaded -> {
            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    items(tracksNow.events) { event ->
                        EventItem(
                            modifier = Modifier
                                .fillParentMaxWidth(0.8f)
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            event = event,
                            favourites = favourites,
                            onClickAction = onNavigate
                        )
                    }
                }
            }
        }
        MainTracksNowState.Loading -> item {
            LoadingItem()
        }
    }
}

fun LazyListScope.preferredTracks(
    preferredTracks: MainPreferredTracksState,
    favourites: FavouriteEventsState,
    onNavigate: (String) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(top= 16.dp, start = 16.dp),
            text = stringResource(R.string.main_your_favourite_tracks),
            style = MaterialTheme.typography.titleSmall
        )
    }
    when(preferredTracks) {
        is MainPreferredTracksState.Loaded -> {
            items(preferredTracks.tracks) { track ->
                TrackRow(
                    track = track,
                    favourites = favourites,
                    onNavigate = onNavigate
                )
            }
        }
        MainPreferredTracksState.Loading -> item {
            LoadingItem()
        }
    }
}

@Composable
fun LoadingItem() {
    LazyRow(
        modifier = Modifier.padding(start = 8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(4) {
            Box(
                modifier = Modifier
                    .fillParentMaxWidth(0.8f)
                    .padding(8.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect(),
            )
        }
    }
}

@Preview
@Composable
fun LoadingItemPreview() {
    MyApplicationTheme {
        LoadingItem()
    }
}
