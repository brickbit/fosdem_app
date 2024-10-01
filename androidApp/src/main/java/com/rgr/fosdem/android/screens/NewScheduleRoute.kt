package com.rgr.fosdem.android.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rgr.fosdem.android.screens.common.EventItem
import com.rgr.fosdem.android.screens.common.LoadingScreen
import com.rgr.fosdem.android.screens.common.NewEventItem
import com.rgr.fosdem.app.viewModel.NewScheduleViewModel
import com.rgr.fosdem.domain.model.BuildingBo
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.RoomBo
import com.rgr.fosdem.domain.model.TalkBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewScheduleRoute(
    viewModel: NewScheduleViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    if(state.isLoading) {
        LoadingScreen()
    } else {
        ScheduleScreen(
            schedules = state.schedules
        )
    }
}

@Composable
fun ScheduleScreen(
    schedules: List<ScheduleBo>
) {
    LazyColumn {
        item {
            TextField(value = "", onValueChange = {})
        }
        items(schedules) { schedule ->
            NewEventItem(
                modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                event = schedule
            ) {

            }
        }
    }
}

