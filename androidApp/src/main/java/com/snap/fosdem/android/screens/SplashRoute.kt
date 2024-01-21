package com.snap.fosdem.android.screens

import android.Manifest
import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.snap.fosdem.android.R
import com.snap.fosdem.app.navigation.Routes
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.state.SplashState
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
        notificationPermissionState.launchPermissionRequest()
        viewModel.saveNotificationPermissionState(
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        )

    }
    when(state) {
        is SplashState.Finished -> {
            LaunchedEffect(Unit) {
                onNavigate(state.route)
            }
        }
        SplashState.Init -> {
            SplashScreen()
        }
        SplashState.Error -> {
            SplashScreen()
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
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
                    contentDescription = stringResource(R.string.splash_fosdem_logo_description)
                )
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

