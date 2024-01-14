package com.snap.fosdem.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.snap.fosdem.android.MyApplicationTheme
import com.snap.fosdem.android.extension.toColor
import com.snap.fosdem.android.screens.common.LoadingScreen
import com.snap.fosdem.app.state.TalkState
import com.snap.fosdem.app.viewModel.TalkViewModel
import com.snap.fosdem.domain.model.EventBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun TalkRoute(
    id: String,
    viewModel: TalkViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.getEvent(id)
    }
    TalkScreen(state = state)
}

@Composable
fun TalkScreen(
    state: TalkState
) {
    when(state) {
        is TalkState.Loaded -> {
            TalkContent(state.event)
        }
        TalkState.Loading -> { LoadingScreen()}
    }
}

@Composable
fun TalkContent(
    event: EventBo
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = event.color.toColor(),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp),
            text = event.talk?.title ?: "",
            style = MaterialTheme.typography.titleMedium
        )


        Column {
            Box(modifier = Modifier.border(width = 3.dp, color = event.color.toColor(), shape = RoundedCornerShape(20.dp))) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = event.talk?.description ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(event.color.toColor()),
                contentAlignment = Alignment.Center
            ) {
                Row() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            model = event.speaker?.image ?: "",
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = event.speaker?.name ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Row {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(event.color.toColor()),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(text = event.talk?.track?: "")
                        Text(text = "Saturday ${event.talk?.day}")
                        Text(text = "${event.talk?.start}/${event.talk?.end}")
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(event.color.toColor()),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(text = event.talk?.room?.name ?: "")
                        Text(text = event.talk?.room?.capacity ?: "")
                        Text(text = event.talk?.room?.location ?: "")
                    }
                }
            }
            Text(text = event.talk?.room?.chat ?: "")
            Text(text = "Notificame cuando empiece el evento")

        }
    }
}

@Preview
@Composable
fun TalkContentPreview(
    event: EventBo
) {
    MyApplicationTheme {
        TalkContent(event = event)
    }
}