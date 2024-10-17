package com.rgr.fosdem.android.screens

import android.Manifest
import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.rgr.fosdem.android.R
import com.rgr.fosdem.app.navigation.Routes
import com.rgr.fosdem.app.viewModel.SplashViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashRoute(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigate: (Routes) -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    val notificationPermissionState = rememberPermissionState(permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else {
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        }
    )

    LaunchedEffect(Unit) {
        viewModel.initializeSplash()
        //notificationPermissionState.launchPermissionRequest()
    }
    LaunchedEffect(state.route != null) {
        state.route?.let { onNavigate(it) }
    }

    SplashScreen()


}

@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
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
                        modifier = Modifier.size(dimensionResource(id = R.dimen.splash_image_size)),
                        painter = painterResource(id = R.drawable.fosdem_logo_inside),
                        contentDescription = stringResource(R.string.splash_fosdem_logo_description)
                    )
                    Image(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.splash_image_size))
                            .rotate(angle),
                        painter = painterResource(id = R.drawable.fosdem_logo_outside),
                        contentDescription = stringResource(R.string.splash_fosdem_logo_description)
                    )
                }
                Text(
                    text = stringResource(R.string.splash_fosdem).uppercase(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL_3A)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}

