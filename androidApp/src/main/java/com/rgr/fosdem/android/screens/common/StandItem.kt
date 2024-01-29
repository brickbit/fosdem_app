package com.rgr.fosdem.android.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.rgr.fosdem.domain.model.StandBo

@Composable
fun StandItem(
    modifier: Modifier = Modifier,
    stand: StandBo,
    onClickItem: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(start= 8.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClickItem() }
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .width(250.dp),
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(250.dp)
                    .height(240.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                model = stand.image,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }
        Text(
            modifier = Modifier
                .width(220.dp)
                .padding(10.dp),
            text = stand.title,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.background),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}