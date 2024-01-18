package com.snap.fosdem.android.screens

import android.net.Uri
import android.webkit.WebView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.snap.fosdem.android.MyApplicationTheme
import com.snap.fosdem.android.R
import com.snap.fosdem.android.mainBrushColor
import com.snap.fosdem.android.screens.common.CustomWebView
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
    val stateNotified = viewModel.stateNotified.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getEvent(id)
    }
    TalkScreen(
        state = state,
        stateNotified = stateNotified,
        notify = { viewModel.isEventNotified(it) },
        notifyEvent = { viewModel.activateEventNotification(it) },
        removeNotifyEvent = { viewModel.disableEventNotification(it) }
    )
}

@Composable
fun TalkScreen(
    state: TalkState,
    stateNotified: Boolean,
    notify: (EventBo) -> Unit,
    notifyEvent: (EventBo) -> Unit,
    removeNotifyEvent: (EventBo) -> Unit
) {
    when(state) {
        is TalkState.Loaded -> {
            LaunchedEffect(Unit) {
                notify(state.event)
            }

            TalkContent(
                event = state.event,
                notify = stateNotified,
                notifyEvent = notifyEvent,
                removeNotifyEvent = removeNotifyEvent
            )
        }
        TalkState.Loading -> { LoadingScreen()}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalkContent(
    event: EventBo,
    notify: Boolean,
    notifyEvent: (EventBo) -> Unit,
    removeNotifyEvent: (EventBo) -> Unit
) {
    var showSpeakerBottomSheet by remember { mutableStateOf(false) }
    val speakerSheetState = rememberModalBottomSheetState()
    var showRoomBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TalkHeader(
            event = event,
            onSpeakerClicked = { showSpeakerBottomSheet = true }
        )
        TalkDescription(
            event = event,
            notify = notify,
            onRoomClicked = { showRoomBottomSheet = true },
            notifyEvent = notifyEvent,
            removeNotifyEvent = removeNotifyEvent
        )
    }
    if(showSpeakerBottomSheet) {
        SpeakerBottomSheet(
            event = event,
            sheetState = speakerSheetState,
            onDismiss = { showSpeakerBottomSheet = false},
        )
    }
    if(showRoomBottomSheet) {
        event.talk?.room?.location?.let {
            val context = LocalContext.current
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(context, Uri.parse(it))
        }
        showRoomBottomSheet = false
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerBottomSheet(
    event: EventBo,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                model = event.speaker?.image ?: "",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                text = event.speaker?.name ?: "",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = event.speaker?.description ?: "",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TalkHeader(
    event: EventBo,
    onSpeakerClicked: () -> Unit
) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = (event.talk?.title ?: "").uppercase(),
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onSpeakerClicked() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                model = event.speaker?.image ?: "",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = event.speaker?.name ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${event.talk?.day ?: ""}  ${event.startHour}/${event.endHour}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun TalkDescription(
    event: EventBo,
    notify: Boolean,
    onRoomClicked: () -> Unit,
    notifyEvent: (EventBo) -> Unit,
    removeNotifyEvent: (EventBo) -> Unit
) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = (event.talk?.track ?: "").uppercase(),
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier
                .clickable { onRoomClicked() },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = (event.talk?.room?.name ?: "").uppercase(),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = event.talk?.description ?: "",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 64.dp)
                .fillMaxWidth()
                .clickable { if (notify) notifyEvent(event) else removeNotifyEvent(event) }
                .background(
                    brush = Brush.linearGradient(colorStops = mainBrushColor),
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 12.dp),
            text = if(notify) "Quitar alarma" else "Poner alarma",
            style = MaterialTheme.typography.bodyMedium.copy(Color.White),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun TalkContentPreview(
    event: EventBo
) {
    MyApplicationTheme {
        TalkContent(
            event = event,
            notify = false,
            notifyEvent = {},
            removeNotifyEvent = {}
        )
    }
}