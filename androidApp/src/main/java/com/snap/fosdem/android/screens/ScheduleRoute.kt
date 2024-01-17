package com.snap.fosdem.android.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.mainBrushColor
import com.snap.fosdem.android.screens.common.FilterDropDownMenu
import com.snap.fosdem.android.screens.common.LoadingScreen
import com.snap.fosdem.app.state.ScheduleState
import com.snap.fosdem.app.viewModel.ScheduleViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduleRoute(
    viewModel: ScheduleViewModel = koinViewModel()
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
                scheduledLoaded = state
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
    scheduledLoaded: ScheduleState.Loaded
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    LazyColumn {
        items(scheduledLoaded.events) { event ->
            Text(text = event.talk?.title ?: "Desconocido")
        }
    }
    Button(onClick = { showBottomSheet = true }) {
        Text(text = "open")
    }
    if (showBottomSheet) {
        ScheduleBottomSheet(
            hours = hours,
            tracks = tracks,
            rooms = rooms,
            scheduledLoaded = scheduledLoaded,
            sheetState = sheetState,
            onDismiss = { showBottomSheet = false},
            filterSchedule = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }
        )
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
    filterSchedule: () -> Unit,
) {
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
                        selectedItem = null,
                        items = listOf("Saturday", "Sunday"),
                        onItemSelected = {}
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
                        selectedItem = null,
                        items = tracks,
                        onItemSelected = {}
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
                                isActive = true,
                                onClick = {}
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
                                isActive = false,
                                onClick = {}
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
                            .clickable { filterSchedule()  }
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

@Composable
fun SelectableChip(
    title: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    if(isActive) {
        Text(
            modifier = Modifier
                .clickable { onClick() }
                .background(
                    brush = Brush.linearGradient(colorStops = mainBrushColor),
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(Color.White)
        )
    } else {
        Text(
            modifier = Modifier
                .clickable { onClick() }
                .border(
                    border = BorderStroke(width = 2.dp, color = Color.Gray),
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(Color.Gray)
        )
    }

}

