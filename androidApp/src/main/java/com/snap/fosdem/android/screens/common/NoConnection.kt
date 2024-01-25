package com.snap.fosdem.android.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.R
import com.snap.fosdem.android.mainBrushColor

@Composable
fun NoConnection(
    onClickAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.size(48.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedPreloader(
                modifier = Modifier.size(140.dp),
                resId = R.raw.no_connection
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.no_connection_description),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 32.dp, end = 32.dp)
                .background(
                    brush = Brush.linearGradient(colorStops = mainBrushColor),
                    shape = CircleShape
                )
                .padding(vertical = 12.dp, horizontal = 32.dp)
                .clickable { onClickAction() },
            text = stringResource(R.string.use_offline_mode),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall.copy(Color.White)
        )
    }
}