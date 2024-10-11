package com.rgr.fosdem.android.screens.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rgr.fosdem.android.R

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box {
            val infiniteTransition = rememberInfiniteTransition(label = "logo")
            val angle by infiniteTransition.animateFloat(
                initialValue = 0F,
                targetValue = 360F,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing)
                ), label = "logo"
            )
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.fosdem_logo_inside),
                contentDescription = stringResource(R.string.splash_fosdem_logo_description)
            )
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .rotate(angle),
                painter = painterResource(id = R.drawable.fosdem_logo_outside),
                contentDescription = stringResource(R.string.splash_fosdem_logo_description)
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "Loading...",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(device = Devices.PIXEL_3A)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}