package com.snap.fosdem.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.R
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.state.SplashState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigate: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.initializeSplash()
    }
    val state = viewModel.state.collectAsState().value
    when(state) {
        SplashState.Finished -> {
            LaunchedEffect(Unit) {
                onNavigate()
            }
        }
        SplashState.Init -> {
            SplashScreen()
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(dimensionResource(id = R.dimen.splash_image_size)),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.fosdem_logo_description)
            )
            Text(
                text = stringResource(R.string.fosdem).uppercase(),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(device = Devices.PIXEL_3A)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}

