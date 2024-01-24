package com.snap.fosdem.android.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.snap.fosdem.domain.model.StandBo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandBottomSheet(
    position: Int,
    stands: List<StandBo>,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .width(250.dp)
                        .height(240.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    model = stands[position].image,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                )
            }
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = stands[position].title,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            stands[position].features.forEach {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${it.subtitle} - ${it.type}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
                items(it.companies) { company ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = company,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item{ Spacer(modifier = Modifier.padding(bottom = 50.dp)) }
        }
    }
}