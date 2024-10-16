package com.rgr.fosdem.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rgr.fosdem.android.R
import com.rgr.fosdem.android.screens.common.EventItem
import com.rgr.fosdem.android.screens.common.LoadingScreen
import com.rgr.fosdem.android.screens.common.NewEventItem
import com.rgr.fosdem.android.screens.common.TitleTopBar
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
            schedules = state.schedules,
            onPressFavourite = { event ->
                if(event.favourite) {
                    viewModel.notNotifyEvent(event)
                } else {
                    viewModel.notNotifyEvent(event)
                }
            }
        )
    }
}

@Composable
fun ScheduleScreen(
    schedules: List<ScheduleBo>,
    onPressFavourite: (ScheduleBo) -> Unit
) {
    val searchText = remember { mutableStateOf("") }
    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                TitleTopBar(stringResource(R.string.schedule))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    shape = RoundedCornerShape(30.dp),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, "", tint = Color.Black)
                    },
                    trailingIcon = {
                        Icon(Icons.Filled.Menu, "", tint = Color.Black)
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Black)
                )
            }
        }
        items(schedules) { schedule ->
            NewEventItem(
                modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                event = schedule,
                onPressFavourite = onPressFavourite,
                onClickAction = {}
            )
        }
    }
}

