package com.snap.fosdem.android.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.R
import com.snap.fosdem.android.mainBrushColor
import com.snap.fosdem.android.screens.common.Chip
import com.snap.fosdem.android.screens.common.FilterDropDownMenu
import com.snap.fosdem.android.screens.common.LoadingScreen
import com.snap.fosdem.android.screens.common.SelectableChip
import com.snap.fosdem.android.transparentBrushColorReversed
import com.snap.fosdem.app.state.ScheduleState
import com.snap.fosdem.app.viewModel.ScheduleViewModel
import com.snap.fosdem.domain.model.EventBo
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
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getHours()
        viewModel.getTracks()
        viewModel.getRooms()
        viewModel.getScheduleBy(
            day = "Saturday",
            hours = emptyList(),
            tracks = emptyList(),
            rooms = emptyList()
        )
    }
    when(state) {
        is ScheduleState.Loaded -> {
            ScheduleScreen(
                hours = stateHour,
                tracks = stateTrack,
                rooms = stateRooms,
                scheduledLoaded = state,
                onFilter = { filter ->
                    viewModel.getScheduleBy(
                        day = filter.day,
                        hours = filter.hours,
                        tracks = filter.tracks,
                        rooms = filter.rooms
                    )
                },
                onEventClicked = onEventClicked
            )
        }
        ScheduleState.Loading -> {
            LoadingScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    hours: List<String>,
    tracks: List<String>,
    rooms: List<String>,
    scheduledLoaded: ScheduleState.Loaded,
    onFilter: (ScheduleState.Loaded) -> Unit,
    onEventClicked: (String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    LazyColumn {
        item { FilterTopBar(onClickAction = {showBottomSheet = true}) }
        item { FiltersUsed(scheduledLoaded = scheduledLoaded)}
        items(scheduledLoaded.events) { event ->
            EventSchedule(
                event = event,
                onClickAction = onEventClicked
            )
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
            }
        )
    }
}

@Composable
fun EventSchedule(
    event: EventBo,
    onClickAction: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(IntrinsicSize.Min)
            .clickable { onClickAction(event.id) }
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                .padding(4.dp)
                .heightIn(120.dp)
                .widthIn(45.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = event.talk?.day?.substring(startIndex = 0, endIndex = 3) ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
            )
            Text(
                text = event.talk?.start ?: "-",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .heightIn(80.dp)
        ) {
            Text(
                text = event.talk?.title ?: "Desconocido",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = event.speaker?.name ?: "Desconocido",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = event.talk?.room?.name ?: "Desconocido",
                style = MaterialTheme.typography.bodySmall
            )
        }
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
                text = "Schedule",
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
    scheduledLoaded: ScheduleState.Loaded
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(colorStops = transparentBrushColorReversed(context))),
    ) {
        ListItem(
            headlineContent = { Text(text = "Tracks") },
            trailingContent = { Text(text = scheduledLoaded.day) },
            supportingContent = {
                Text(text = scheduledLoaded.tracks.getOrNull(0) ?: "All")
            }
        )
        if(scheduledLoaded.hours.isNotEmpty()) {
            ListItem(
                headlineContent = { Text(text = "Hours") },
                supportingContent = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(scheduledLoaded.hours) { hour ->
                            Chip(title = hour)
                        }
                    }
                }
            )
        }
        if(scheduledLoaded.rooms.isNotEmpty()) {
            ListItem(
                headlineContent = { Text(text = "Rooms") },
                supportingContent = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(scheduledLoaded.rooms) { room ->
                            Chip(title = room)
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
    scheduledLoaded: ScheduleState.Loaded,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    filterSchedule: (ScheduleState.Loaded) -> Unit,
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
                        text = "Filtrar por",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
            ListItem(
                headlineContent = {
                    Text(
                        text = "Día",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }, supportingContent = {
                    FilterDropDownMenu(
                        selectedItem = currentData.day,
                        items = listOf("Saturday", "Sunday"),
                        onItemSelected = { currentData = currentData.copy(day = it) }
                    )
                }
            )
            ListItem(
                headlineContent = {
                    Text(
                        text = "Tracks",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                supportingContent = {
                    FilterDropDownMenu(
                        selectedItem = currentData.tracks.getOrNull(0),
                        items = tracks,
                        onItemSelected = { currentData = currentData.copy(tracks = listOf(it)) }
                    )
                }
            )
            ListItem(
                headlineContent = {
                    Text(
                        text = "Hora",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                supportingContent = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(hours){
                            SelectableChip(
                                title = it,
                                isActive = currentData.hours.contains(it),
                                onClick = { selectedHour ->
                                    currentData = if(currentData.hours.contains(selectedHour)) {
                                        val listHours = currentData.hours.toMutableList()
                                        listHours.remove(selectedHour)
                                        currentData.copy(hours = listHours)
                                    } else {
                                        val listHours = currentData.hours.toMutableList()
                                        listHours.add(selectedHour)
                                        currentData.copy(hours = listHours)
                                    }
                                }
                            )
                        }
                    }
                }
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = "Room",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                supportingContent = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(rooms){
                            SelectableChip(
                                title = it,
                                isActive = scheduledLoaded.rooms.contains(it),
                                onClick = { selectedRoom ->
                                    currentData = if(currentData.rooms.contains(selectedRoom)) {
                                        val listRooms = currentData.rooms.toMutableList()
                                        listRooms.remove(selectedRoom)
                                        currentData.copy(rooms = listRooms)
                                    } else {
                                        val listRooms = currentData.rooms.toMutableList()
                                        listRooms.add(selectedRoom)
                                        currentData.copy(rooms = listRooms)
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
                        text = "Filter",
                        style = MaterialTheme.typography.bodyMedium.copy(Color.White),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    }
}


