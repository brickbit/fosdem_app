package com.snap.fosdem.android.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.R
import com.snap.fosdem.android.extension.dayMiniToTranslatable
import com.snap.fosdem.app.state.FavouriteEventsState
import com.snap.fosdem.domain.model.EventBo

@Composable
fun EventItem(
    modifier: Modifier = Modifier,
    event: EventBo,
    favourites: FavouriteEventsState,
    onClickAction: (String) -> Unit
) {
    val context = LocalContext.current
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
                .heightIn(110.dp + (event.speaker.count() * 15).dp)
                .widthIn(45.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(favourites is FavouriteEventsState.Loaded && favourites.events.contains(event)) {
                Image(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            } else {
                Spacer(modifier = Modifier.size(16.dp))
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = event.talk.day.substring(startIndex = 0, endIndex = 3).dayMiniToTranslatable(context),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
                )
                Text(
                    text = event.talk.start,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))

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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(16.dp),
                        painter = painterResource(id = R.drawable.ic_account),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                Text(
                    text = event.talk.room.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}