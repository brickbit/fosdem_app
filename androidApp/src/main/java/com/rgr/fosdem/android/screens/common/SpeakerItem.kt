package com.rgr.fosdem.android.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rgr.fosdem.android.R
import com.rgr.fosdem.domain.model.SpeakerBo

@Composable
fun SpeakerItem(
    modifier: Modifier = Modifier,
    speaker: SpeakerBo,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .width(120.dp),
        ) {
            if (speaker.image != null) {
                AsyncImage(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    model = speaker.image,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            } else {
                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }
        }
        Text(
            modifier = Modifier
                .width(100.dp)
                .padding(10.dp),
            text = speaker.name,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}