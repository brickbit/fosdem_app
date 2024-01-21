package com.snap.fosdem.android.screens.common

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.snap.fosdem.domain.model.EventBo

@Composable
fun EventItem(
    modifier: Modifier = Modifier,
    event: EventBo,
    onClickAction: (String) -> Unit
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clickable { onClickAction(event.id) }
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
                .padding(4.dp)
                .heightIn(110.dp + (event.speaker.count()*10).dp)
                .widthIn(45.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = event.talk.day.substring(startIndex = 0, endIndex = 3),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
            )
            Text(
                text = event.talk.start,
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
                text = event.talk.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            event.speaker.forEach {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = event.talk.room.name,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}