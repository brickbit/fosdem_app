package com.snap.fosdem.android.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import com.snap.fosdem.android.BuildConfig
import com.snap.fosdem.android.MainActivity
import com.snap.fosdem.android.MyApplicationTheme
import com.snap.fosdem.android.R
import com.snap.fosdem.android.extension.splitImage
import com.snap.fosdem.android.mainBrushColor
import com.snap.fosdem.android.screens.common.EventItem
import com.snap.fosdem.android.screens.common.SpeakerBottomSheet
import com.snap.fosdem.android.screens.common.SpeakerItem
import com.snap.fosdem.android.screens.common.StandBottomSheet
import com.snap.fosdem.android.screens.common.StandItem
import com.snap.fosdem.android.screens.common.shimmerEffect
import com.snap.fosdem.app.state.FavouriteEventsState
import com.snap.fosdem.app.state.MainPreferredTracksState
import com.snap.fosdem.app.state.MainTracksNowState
import com.snap.fosdem.app.state.SpeakersState
import com.snap.fosdem.app.state.StandsState
import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.domain.model.TrackBo
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainRoute(
    viewModel: MainViewModel = koinViewModel(),
    onNavigate: (String) -> Unit,
    navigateToSchedule: () -> Unit,
) {
    val preferredTracksState = viewModel.statePreferredTracks.collectAsState().value
    val tracksNowState = viewModel.stateCurrentTracks.collectAsState().value
    val favouriteEventsState = viewModel.stateFavouriteEvents.collectAsState().value
    val speakerState = viewModel.stateSpeaker.collectAsState().value
    val standState = viewModel.stateStand.collectAsState().value
    val refreshing = viewModel.isRefreshing.collectAsState().value
    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.onRefresh() })
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getScheduleByMoment()
        viewModel.getPreferredTracks()
        viewModel.getFavouritesEvents()
        viewModel.getSpeakerList()
        viewModel.getStandList()
    }

    BackHandler {
        (context as MainActivity).finish()
    }

    Box {
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f)
        )
        MainScreen(
            modifier = Modifier
                .pullRefresh(pullRefreshState),
            preferredTracks = preferredTracksState,
            tracksNow = tracksNowState,
            favourites = favouriteEventsState,
            speakers = speakerState,
            stands = standState,
            onNavigate = onNavigate,
            navigateToSchedule = navigateToSchedule
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    preferredTracks: MainPreferredTracksState,
    tracksNow: MainTracksNowState,
    favourites: FavouriteEventsState,
    speakers: SpeakersState,
    stands: StandsState,
    onNavigate: (String) -> Unit,
    navigateToSchedule: () -> Unit
) {
    var showSpeakerBottomSheet by remember { mutableStateOf( Pair(0,false)) }
    val speakerSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showStandBottomSheet by remember { mutableStateOf( Pair(0,false)) }
    val standSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if(showSpeakerBottomSheet.second) {
        SpeakerBottomSheet(
            position = showSpeakerBottomSheet.first,
            speakers = (speakers as? SpeakersState.Loaded)?.speakers ?: emptyList(),
            sheetState = speakerSheetState,
            onDismiss = { showSpeakerBottomSheet = Pair(showSpeakerBottomSheet.first,false)},
        )
    }
    if(showStandBottomSheet.second) {
        StandBottomSheet(
            position = showStandBottomSheet.first,
            stands = (stands as? StandsState.Loaded)?.stands ?: emptyList(),
            sheetState = standSheetState,
            onDismiss = { showStandBottomSheet = Pair(showStandBottomSheet.first,false) }
        )
    }

    LazyColumn(modifier = modifier) {
        item {
            ScheduleCard(navigateToSchedule = navigateToSchedule)
        }
        rightNowItems(
            tracksNow = tracksNow,
            favourites = favourites,
            onNavigate = onNavigate
        )

        favouriteEvents(
            favourites = favourites,
            onNavigate = onNavigate
        )
        speakerItems(
            speakers = speakers,
            onNavigate = { showSpeakerBottomSheet = Pair(it, true) }
        )
        preferredTracks(
            preferredTracks = preferredTracks,
            favourites = favourites,
            onNavigate = onNavigate
        )
        item {
            PlaceComposable()
        }
        standItems(
            stands = stands,
            onNavigate = { showStandBottomSheet = Pair(it, true) }
        )
    }
}

@Composable
fun PlaceComposable() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.main_how_to_get),
            style = MaterialTheme.typography.bodyMedium
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.location))
                    context.startActivity(browserIntent)
                },
            painter = painterResource(id = R.drawable.ic_place),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
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

    when(tracksNow) {
        is MainTracksNowState.Loaded -> {
            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.main_right_now),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
        MainTracksNowState.Empty -> item {
            EmptySection(
                title = stringResource(R.string.main_right_now),
                description = stringResource(R.string.main_talks_right_now)
            )
        }
    }
}

fun LazyListScope.favouriteEvents(
    favourites: FavouriteEventsState,
    onNavigate: (String) -> Unit
) {

    when(favourites) {
        is FavouriteEventsState.Loaded -> {
            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.main_your_favourites_talks),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    items(favourites.events) { event ->
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
        FavouriteEventsState.Loading -> item {
            LoadingItem()
        }
        FavouriteEventsState.Empty -> item {
            EmptySection(
                title = stringResource(R.string.main_your_favourites_talks),
                description = stringResource(R.string.main_favourite_events),
            )
        }
    }
}

@Composable
fun EmptySection(
    title: String,
    description: String,
) {
    Column {
        Text(
            modifier = Modifier.padding(top= 16.dp, start = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun LazyListScope.speakerItems(
    speakers: SpeakersState,
    onNavigate: (Int) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.main_speakers),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    when(speakers) {
        is SpeakersState.Loaded -> {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    itemsIndexed(speakers.speakers) { index, speaker ->
                        SpeakerItem(
                            modifier = Modifier.clickable { onNavigate(index) },
                            speaker = speaker
                        )
                    }
                }
            }
        }
        SpeakersState.Loading -> item {
            LoadingItem()
        }
    }
}

fun LazyListScope.standItems(
    stands: StandsState,
    onNavigate: (Int) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.main_stands),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    when(stands) {
        is StandsState.Loaded -> {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    itemsIndexed(stands.stands) { index, stand ->
                        StandItem(
                            stand = stand,
                            onClickItem = { onNavigate(index) }
                        )
                    }
                }
            }
        }
        StandsState.Loading -> item {
            LoadingItem()
        }
    }
}


fun LazyListScope.preferredTracks(
    preferredTracks: MainPreferredTracksState,
    favourites: FavouriteEventsState,
    onNavigate: (String) -> Unit
) {
    when(preferredTracks) {
        is MainPreferredTracksState.Loaded -> {
            item {
                Text(
                    modifier = Modifier.padding(top= 16.dp, start = 16.dp),
                    text = stringResource(R.string.main_your_favourite_tracks),
                    style = MaterialTheme.typography.titleSmall
                )
            }
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
        MainPreferredTracksState.Empty -> item {
            EmptySection(
                title = stringResource(R.string.main_your_favourite_tracks),
                description = stringResource(R.string.main_select_favourite_tracks),
            )
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
