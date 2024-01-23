package com.snap.fosdem.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.R
import com.snap.fosdem.android.extension.dayFromTranslatable
import com.snap.fosdem.android.extension.dayToTranslatable
import com.snap.fosdem.android.mainBrushColor
import com.snap.fosdem.android.screens.common.AnimatedPreloader
import com.snap.fosdem.android.screens.common.Chip
import com.snap.fosdem.android.screens.common.EventItem
import com.snap.fosdem.android.screens.common.FilterDropDownMenu
import com.snap.fosdem.android.screens.common.LoadingScreen
import com.snap.fosdem.android.screens.common.SelectableChip
import com.snap.fosdem.android.transparentBrushColorReversed
import com.snap.fosdem.app.state.FavouriteEventsState
import com.snap.fosdem.app.state.ScheduleFilter
import com.snap.fosdem.app.state.ScheduleState
import com.snap.fosdem.app.viewModel.ScheduleViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduleRoute(
    viewModel: ScheduleViewModel = koinViewModel(),
    onEventClicked: (String) -> Unit
) {
    val stateHour = viewModel.stateHour.collectAsState().value
    val stateTrack = viewModel.stateTracks.collectAsState().value
    val stateRooms = viewModel.stateRooms.collectAsState().value
    val favouriteEventsState = viewModel.stateFavouriteEvents.collectAsState().value
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getHours(day = "Saturday")
        viewModel.getTracks()
        viewModel.getRooms()
        viewModel.getFavouritesEvents()
        viewModel.getScheduleBy(
            day = "Saturday",
            hours = emptyList(),
            track = "",
            room = ""
        )
    }
    when(state) {
        is ScheduleState.Loaded -> {
            ScheduleScreen(
                hours = stateHour,
                tracks = stateTrack,
                rooms = stateRooms,
                scheduledLoaded = state.filter,
                favourites = favouriteEventsState,
                onFilter = { filter ->
                    viewModel.getScheduleBy(
                        day = filter.day,
                        hours = filter.hours,
                        track = filter.track,
                        room = filter.room
                    )
                },
                onEventClicked = onEventClicked,
                updateHour = { filter ->
                    viewModel.getHours(filter.day)
                    viewModel.getScheduleBy(
                        day = filter.day,
                        hours = filter.hours,
                        track = filter.track,
                        room = filter.room
                    )
                }
            )
        }
        is ScheduleState.Empty -> {
            ScheduleScreen(
                empty = true,
                hours = stateHour,
                tracks = stateTrack,
                rooms = stateRooms,
                scheduledLoaded = state.filter,
                favourites = favouriteEventsState,
                onFilter = { filter ->
                    viewModel.getScheduleBy(
                        day = filter.day,
                        hours = filter.hours,
                        track = filter.track,
                        room = filter.room
                    )
                },
                onEventClicked = onEventClicked,
                updateHour = { filter ->
                    viewModel.getHours(filter.day)
                    viewModel.getScheduleBy(
                        day = filter.day,
                        hours = filter.hours,
                        track = filter.track,
                        room = filter.room
                    )
                }
            )
        }
        ScheduleState.Loading -> {
            LoadingScreen()
        }
    }
}

@Composable
fun NoItemFound() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedPreloader(modifier = Modifier.size(200.dp))
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.schedule_no_items_found),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    empty: Boolean = false,
    hours: List<String>,
    tracks: List<String>,
    rooms: List<String>,
    scheduledLoaded: ScheduleFilter,
    favourites: FavouriteEventsState,
    onFilter: (ScheduleFilter) -> Unit,
    onEventClicked: (String) -> Unit,
    updateHour: (ScheduleFilter) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LazyColumn {
        item { FilterTopBar(onClickAction = { showBottomSheet = true }) }
        item {
            FiltersUsed(
                scheduledLoaded = scheduledLoaded,
                onFilterResultClicked = { showBottomSheet = true }
            )
        }
        if(!empty) {
            items(scheduledLoaded.events) { event ->
                EventItem(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    event = event,
                    favourites = favourites,
                    onClickAction = onEventClicked
                )
            }
        } else {
            item {
                NoItemFound()
            }
        }
    }

    if (showBottomSheet) {
        ScheduleBottomSheet(
            hours = hours,
            tracks = tracks,
            rooms = rooms,
            scheduledLoaded = scheduledLoaded,
            sheetState = sheetState,
            onDismiss = { showBottomSheet = false},
            filterSchedule = {filters ->
                onFilter(filters)
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            },
            updateHour = updateHour
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTopBar(
    onClickAction: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.schedule),
                style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(30.dp)
                    .clickable { onClickAction() },
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        }
    )
}
@Composable
fun FiltersUsed(
    scheduledLoaded: ScheduleFilter,
    onFilterResultClicked: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(colorStops = transparentBrushColorReversed(context))),
    ) {
        ListItem(
            headlineContent = { Text(text = stringResource(R.string.schedule_day)) },
            trailingContent = {
                Text(
                    modifier = Modifier.clickable { onFilterResultClicked() },
                    text = scheduledLoaded.day.dayToTranslatable(context),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        )
        ListItem(
            headlineContent = { Text(text = stringResource(R.string.schedule_tracks)) },
            trailingContent = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clickable { onFilterResultClicked() },
                    text = if(scheduledLoaded.track == "" || scheduledLoaded.track == "All") { stringResource(R.string.schedule_all) } else { scheduledLoaded.track },
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End
                )
            }
        )
        ListItem(
            headlineContent = { Text(text = stringResource(R.string.schedule_rooms)) },
            trailingContent = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clickable { onFilterResultClicked() },
                    text = if (scheduledLoaded.room == "" || scheduledLoaded.room == "All") { stringResource(R.string.schedule_all) } else { scheduledLoaded.room },
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End
                )
            },
        )
        if(scheduledLoaded.hours.isNotEmpty()) {
            val listState = rememberLazyListState()
            ListItem(
                headlineContent = { Text(text = stringResource(R.string.schedule_hour)) },
                supportingContent = {
                    LazyRow(
                        state = listState,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(scheduledLoaded.hours) { hour ->
                            Chip(
                                modifier = Modifier.clickable { onFilterResultClicked() },
                                title = hour
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheet(
    hours: List<String>,
    tracks: List<String>,
    rooms: List<String>,
    scheduledLoaded: ScheduleFilter,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    filterSchedule: (ScheduleFilter) -> Unit,
    updateHour: (ScheduleFilter) -> Unit
) {
    var currentData = scheduledLoaded

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(bottom = 64.dp)
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = stringResource(R.string.schedule_filter_by),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(R.string.schedule_day),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }, supportingContent = {
                    val context = LocalContext.current
                    FilterDropDownMenu(
                        selectedItem = currentData.day.dayToTranslatable(context),
                        items = listOf("Saturday".dayToTranslatable(context), "Sunday".dayToTranslatable(context)),
                        onItemSelected = {
                            currentData = currentData.copy(day = it.dayFromTranslatable(context))
                            updateHour(currentData)
                        }
                    )
                }
            )
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.schedule_tracks),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                supportingContent = {
                    FilterDropDownMenu(
                        selectedItem = if(currentData.track == "" || currentData.track == "All") { stringResource(R.string.schedule_all) } else { currentData.track },
                        items = tracks,
                        onItemSelected = { currentData = currentData.copy(track = it) }
                    )
                }
            )
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(R.string.schedule_room),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                supportingContent = {
                    FilterDropDownMenu(
                        selectedItem = if (currentData.room == "" || currentData.room == "All") { stringResource(R.string.schedule_all) } else { currentData.room },
                        items = rooms,
                        onItemSelected = { currentData = currentData.copy(room = it) }
                    )
                }
            )
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.schedule_hour),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                supportingContent = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(hours){
                            var composableHours by remember {
                                mutableStateOf(currentData.hours)
                            }
                            SelectableChip(
                                title = it,
                                isActive = composableHours.contains(it),
                                onClick = { selectedHour ->
                                    currentData = if(currentData.hours.contains(selectedHour)) {
                                        val listHours = currentData.hours.toMutableList()
                                        listHours.remove(selectedHour)
                                        composableHours = listHours
                                        currentData.copy(hours = listHours)
                                    } else {
                                        val listHours = currentData.hours.toMutableList()
                                        listHours.add(selectedHour)
                                        composableHours = listHours
                                        currentData.copy(hours = listHours)
                                    }
                                }
                            )
                        }
                    }
                }
            )
            ListItem(
                modifier = Modifier.padding(top = 16.dp),
                headlineContent = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { filterSchedule(currentData) }
                            .background(
                                brush = Brush.linearGradient(colorStops = mainBrushColor),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(vertical = 12.dp),
                        text = stringResource(R.string.schedule_filter),
                        style = MaterialTheme.typography.bodyMedium.copy(Color.White),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    }
}


