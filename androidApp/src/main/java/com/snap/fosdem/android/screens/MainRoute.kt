package com.snap.fosdem.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.snap.fosdem.android.MyApplicationTheme
import com.snap.fosdem.android.R
import com.snap.fosdem.android.extension.splitImage
import com.snap.fosdem.android.extension.toColor
import com.snap.fosdem.android.screens.common.shimmerEffect
import com.snap.fosdem.app.state.MainPreferredTracksState
import com.snap.fosdem.app.state.MainTracksBuildingState
import com.snap.fosdem.app.state.MainTracksNowState
import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.TrackBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoute(
    viewModel: MainViewModel = koinViewModel(),
    onNavigate: (String) -> Unit,
    navigateToAgenda: () -> Unit,
) {
    val preferredTracksState = viewModel.statePreferredTracks.collectAsState().value
    val tracksNowState = viewModel.stateCurrentTracks.collectAsState().value
    val tracksBuildingState = viewModel.stateBuildingTracks.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getScheduleByMoment()
        viewModel.getPreferredTracks()
        viewModel.getScheduleByBuilding()
    }
    MainScreen(
        preferredTracks = preferredTracksState,
        tracksNow = tracksNowState,
        tracksBuilding = tracksBuildingState,
        onNavigate = onNavigate,
        navigateToAgenda = navigateToAgenda
    )
}

@Composable
fun MainScreen(
    preferredTracks: MainPreferredTracksState,
    tracksNow: MainTracksNowState,
    tracksBuilding: MainTracksBuildingState,
    onNavigate: (String) -> Unit,
    navigateToAgenda: () -> Unit
) {
    LazyColumn {
        item {
            MainHeader()
        }
        rightNowItems(
            tracksNow = tracksNow,
            onNavigate = onNavigate
        )
        item { Spacer(modifier = Modifier.height(24.dp)) }
        tracksByBuilding(
            tracksBuilding = tracksBuilding,
            onNavigate = onNavigate
        )
        item {
            AgendaCard (navigateToAgenda = navigateToAgenda)
        }
        preferredTracks(
            preferredTracks = preferredTracks,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun TrackRow(
    track: TrackBo,
    onNavigate: (String) -> Unit
) {
    Column {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
            text = track.name,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        LazyRow(
            modifier = Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(track.events) { event ->
                EventItem(
                    event = event,
                    onNavigate = onNavigate
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(
    event: EventBo,
    onNavigate: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = event.color.toColor()),
        onClick = { onNavigate(event.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(16.dp)
                .height(100.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.width(260.dp),
                text = event.talk?.title ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
                Row(
                    modifier = Modifier.width(260.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CardImageEvent(image = event.speaker?.image)
                    Text(
                        modifier = Modifier.width(150.dp),
                        text = event.speaker?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier.padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(20.dp),
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null
                    )
                    Text(
                        text = "${event.talk?.start}\n${event.talk?.end}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun CardImageEvent(
    image: String?,
) {
    if (image != null) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = CircleShape)
                .size(30.dp),
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                model = image,
                contentDescription = null,
            )
        }

    } else {
        Image(
            modifier = Modifier
                .size(30.dp),
            painter = painterResource(id = R.drawable.ic_account),
            contentDescription = null
        )
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
            contentDescription = null
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                colorFilter = ColorFilter.tint(color = Color.White),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            Row {
                Text(
                    modifier = Modifier
                        .background(Color.Black)
                        .padding(8.dp),
                    text = "Bruselas | 3 & 4 Febrero 2024",
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaCard(
    navigateToAgenda: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 24.dp
        ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        onClick = { navigateToAgenda()}
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
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier
                    .width(150.dp)
                    .padding(vertical = 24.dp),
                text = "Consulta ahora la agenda completa",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            ContextCompat.getDrawable(context,R.drawable.ic_launcher_foreground).splitImage()?.
            first?.asImageBitmap()?.let {
                Image(
                    modifier = Modifier.height(120.dp),
                    contentScale = ContentScale.FillHeight,
                    bitmap = it,
                    contentDescription = null
                )
            }
        }
    }
}

fun LazyListScope.rightNowItems(
    tracksNow: MainTracksNowState,
    onNavigate: (String) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Ahora mismo",
            style = MaterialTheme.typography.titleMedium
        )
    }
    when(tracksNow) {
        is MainTracksNowState.Loaded -> {
            item {
                LazyRow(
                    modifier = Modifier.padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(tracksNow.events) { event ->
                        EventItem(
                            event = event,
                            onNavigate = onNavigate
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

fun LazyListScope.tracksByBuilding(
    tracksBuilding: MainTracksBuildingState,
    onNavigate: (String) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Más cercanos",
            style = MaterialTheme.typography.titleMedium
        )
    }

    when(tracksBuilding) {
        is MainTracksBuildingState.Loaded -> {
            item {
                LazyRow(
                    modifier = Modifier.padding(start = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(tracksBuilding.events) { event ->
                        EventItem(
                            event = event,
                            onNavigate = onNavigate
                        )
                    }
                }
            }
        }
        MainTracksBuildingState.Loading -> item{
            LoadingItem()
        }
    }
}

fun LazyListScope.preferredTracks(
    preferredTracks: MainPreferredTracksState,
    onNavigate: (String) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Tus tracks preferidos",
            style = MaterialTheme.typography.titleMedium
        )
    }
    when(preferredTracks) {
        is MainPreferredTracksState.Loaded -> {
            items(preferredTracks.tracks) { track ->
                TrackRow(
                    track = track,
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
        modifier = Modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(4) {
            Box(
                modifier = Modifier
                    .fillParentMaxWidth(0.6f)
                    .padding(top = 16.dp)
                    .height(100.dp)
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
