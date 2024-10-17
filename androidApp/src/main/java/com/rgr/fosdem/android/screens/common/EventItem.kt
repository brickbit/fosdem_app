package com.rgr.fosdem.android.screens.common

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rgr.fosdem.android.R
import com.rgr.fosdem.android.extension.dayMiniToTranslatable
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo

@Composable
fun EventItem(
    modifier: Modifier = Modifier,
    event: ScheduleBo,
    favourites: List<ScheduleBo>,
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
                .height(135.dp)
                .widthIn(45.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(favourites.contains(event)) {
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
                    text = event.date.substring(startIndex = 0, endIndex = 3).dayMiniToTranslatable(context),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
                )
                Text(
                    text = event.start,
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
                modifier = Modifier.height(80.dp),
                text = event.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                modifier = Modifier
                    .height(35.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                            text = it,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
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
                    text = event.room,
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}


@Composable
fun NewEventItem(
    modifier: Modifier = Modifier,
    event: ScheduleBo,
    onClickAction: (String) -> Unit,
    onPressFavourite: (ScheduleBo) -> Unit,
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
                .height(135.dp)
                .widthIn(45.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .height(135.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = event.date.substring(IntRange(8,9)),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background)
                )
                Text(
                    text = event.start,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(80.dp),
                    text = event.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier.size(40.dp),
                    onClick = { onPressFavourite(event) },
                ) {
                    if (event.favourite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .height(35.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                            text = it,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            /*Row(
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
            }*/

        }
    }
}