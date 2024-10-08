package com.rgr.fosdem.android.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rgr.fosdem.android.BuildConfig
import com.rgr.fosdem.android.MainActivity
import com.rgr.fosdem.android.MyApplicationTheme
import com.rgr.fosdem.android.R
import com.rgr.fosdem.android.mainBrushColor
import com.rgr.fosdem.android.screens.common.EventItem
import com.rgr.fosdem.android.screens.common.SpeakerBottomSheet
import com.rgr.fosdem.android.screens.common.SpeakerItem
import com.rgr.fosdem.android.screens.common.StandBottomSheet
import com.rgr.fosdem.android.screens.common.StandItem
import com.rgr.fosdem.android.screens.common.shimmerEffect
import com.rgr.fosdem.app.viewModel.HomeViewModel
import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigate: (String) -> Unit,
    navigateToWebSchedule: (String) -> Unit,
    onSeeAllClicked: (String, String) -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    BackHandler {
        (context as MainActivity).finish()
    }

    Box {
        HomeScreen(
            modifier = Modifier,
            tracksNow = state.rightNowSchedules,
            isLoading = state.isLoading,
            favourites = state.favouriteSchedules,
            speakers = state.speakers,
            stands = state.stands,
            onNavigate = onNavigate,
            navigateToWebSchedule = navigateToWebSchedule,
            onSeeAllClicked = onSeeAllClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    tracksNow: List<ScheduleBo>,
    isLoading: Boolean,
    favourites: List<ScheduleBo>,
    speakers: List<SpeakerBo>,
    stands: List<StandBo>,
    onNavigate: (String) -> Unit,
    navigateToWebSchedule: (String) -> Unit,
    onSeeAllClicked: (String, String) -> Unit
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
            speakers = speakers,
            sheetState = speakerSheetState,
            onDismiss = { showSpeakerBottomSheet = Pair(showSpeakerBottomSheet.first,false)},
        )
    }
    if(showStandBottomSheet.second) {
        StandBottomSheet(
            position = showStandBottomSheet.first,
            stands = stands,
            sheetState = standSheetState,
            onDismiss = { showStandBottomSheet = Pair(showStandBottomSheet.first,false) }
        )
    }

    LazyColumn(modifier = modifier) {
        rightNowItems(
            isLoading = isLoading,
            tracksNow = tracksNow,
            favourites = favourites,
            onNavigate = onNavigate,
            onSeeAllClicked = { title -> onSeeAllClicked(title, "CurrentEvents") }
        )

        favouriteEvents(
            isLoading = isLoading,
            favourites = favourites,
            onNavigate = onNavigate,
            onSeeAllClicked = { title -> onSeeAllClicked(title, "FavoriteEvents") }
        )
        item {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp, Alignment.CenterHorizontally)
            ) {
                CheckScheduleWeb(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.main_saturday_schedule),
                    url = BuildConfig.scheduleSaturday,
                    openWebView = { navigateToWebSchedule(it) }
                )
                CheckScheduleWeb(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.main_sunday_schedule),
                    url = BuildConfig.scheduleSunday,
                    openWebView = { navigateToWebSchedule(it) }
                )
            }
        }
        speakerItems(
            isLoading = isLoading,
            speakers = speakers,
            onNavigate = { showSpeakerBottomSheet = Pair(it, true) }
        )
        item {
            PlaceComposable()
        }
        standItems(
            isLoading = isLoading,
            stands = stands,
            onNavigate = { showStandBottomSheet = Pair(it, true) }
        )
    }
}

@Composable
fun CheckScheduleWeb(
    modifier: Modifier = Modifier,
    title: String,
    url: String,
    openWebView: (String) -> Unit
) {
    Column(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(colorStops = mainBrushColor),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(
                horizontal = 8.dp,
                vertical = 16.dp
            )
            .clickable { openWebView(url) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(Color.White),
            textAlign = TextAlign.Center
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

fun LazyListScope.rightNowItems(
    isLoading: Boolean,
    tracksNow: List<ScheduleBo>,
    favourites: List<ScheduleBo>,
    onNavigate: (String) -> Unit,
    onSeeAllClicked: (String) -> Unit
) {
    if (isLoading) {
        item { LoadingItem() }
    } else {
        if (tracksNow.isEmpty()) {
            item {
                EmptySection(
                    title = stringResource(R.string.main_right_now),
                    description = stringResource(R.string.main_talks_right_now)
                )
            }
        } else {
            item {
                val context = LocalContext.current

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.main_right_now),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { onSeeAllClicked(context.getString(R.string.main_right_now)) },
                        text = stringResource(R.string.main_see_all),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    items(tracksNow) { event ->
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
    }
}

fun LazyListScope.favouriteEvents(
    isLoading: Boolean,
    favourites: List<ScheduleBo>,
    onNavigate: (String) -> Unit,
    onSeeAllClicked: (String) -> Unit
) {
    if(isLoading) {
        item {
            LoadingItem()
        }
    } else {
        if(favourites.isEmpty()) {
            item {
                val context = LocalContext.current

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.main_your_favourites_talks),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { onSeeAllClicked(context.getString(R.string.main_your_favourites_talks)) },
                        text = stringResource(R.string.main_see_all),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    items(favourites) { event ->
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
        } else {
            item {
                EmptySection(
                    title = stringResource(R.string.main_your_favourites_talks),
                    description = stringResource(R.string.main_favourite_events),
                )
            }
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
    isLoading: Boolean,
    speakers: List<SpeakerBo>,
    onNavigate: (Int) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.main_speakers),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    if(isLoading) {
        item { LoadingItem() }
    } else {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp),
            ) {
                itemsIndexed(speakers) { index, speaker ->
                    SpeakerItem(
                        modifier = Modifier.clickable { onNavigate(index) },
                        speaker = speaker
                    )
                }
            }
        }
    }
}

fun LazyListScope.standItems(
    isLoading: Boolean,
    stands: List<StandBo>,
    onNavigate: (Int) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.main_stands),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    if(isLoading) {
        item { LoadingItem() }
    } else {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                itemsIndexed(stands) { index, stand ->
                    StandItem(
                        stand = stand,
                        onClickItem = { onNavigate(index) }
                    )
                }
            }
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
