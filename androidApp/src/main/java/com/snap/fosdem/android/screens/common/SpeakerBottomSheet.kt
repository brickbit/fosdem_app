package com.snap.fosdem.android.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.model.SpeakerBo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerBottomSheet(
    position: Int,
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
                model = event.speaker[position].image ?: "",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = event.speaker[position].name,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = event.speaker[position].description ?: "",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerBottomSheet(
    position: Int,
    speakers: List<SpeakerBo>,
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
                model = speakers.getOrNull(position)?.image ?: "",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = speakers.getOrNull(position)?.name ?: "",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = speakers.getOrNull(position)?.description ?: "",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}