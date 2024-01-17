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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.snap.fosdem.android.R
import com.snap.fosdem.android.screens.common.GrantPermission
import com.snap.fosdem.app.navigation.Routes
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.state.SplashState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigate: (Routes) -> Unit
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.initializeSplash()
    }
    when(state) {
        is SplashState.Finished -> {
            LaunchedEffect(Unit) {
                onNavigate(state.route)
            }
        }
        SplashState.Init -> {
            SplashScreen(
                onNotificationPermissionGranted = { viewModel.saveNotificationPermissionState(it) },
                onLocationPermissionGranted = { fine, coarse -> viewModel.saveLocationPermissionState(fine,coarse)}
            )
        }
        SplashState.Error -> {
            SplashScreen(
                onNotificationPermissionGranted = { viewModel.saveNotificationPermissionState(it) },
                onLocationPermissionGranted = { fine, coarse -> viewModel.saveLocationPermissionState(fine,coarse)}
            )
        }
    }
}

@Composable
fun SplashScreen(
    onNotificationPermissionGranted: (Boolean) -> Unit,
    onLocationPermissionGranted: (Boolean, Boolean) -> Unit
) {
    var fineLocationGranted by remember { mutableStateOf(false) }
    var coarseLocationGranted by remember { mutableStateOf(false) }

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
                    contentDescription = stringResource(R.string.fosdem_logo_description)
                )
                Text(
                    text = stringResource(R.string.fosdem).uppercase(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            GrantPermission(
                permission = Manifest.permission.POST_NOTIFICATIONS,
                onPermissionGranted = { onNotificationPermissionGranted(it) },
            )
        } else {
            GrantPermission(
                permission = Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                onPermissionGranted = { onNotificationPermissionGranted(it) },
            )
        }
        GrantPermission(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {
                fineLocationGranted = it
                onLocationPermissionGranted(fineLocationGranted,coarseLocationGranted)
            },
        )
        GrantPermission(
            permission = Manifest.permission.ACCESS_COARSE_LOCATION,
            onPermissionGranted = {
                coarseLocationGranted = it
                onLocationPermissionGranted(fineLocationGranted,coarseLocationGranted)
            },
        )
    }
}

@Preview(device = Devices.PIXEL_3A)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        onNotificationPermissionGranted = {},
        onLocationPermissionGranted = {_,_ ->}
    )
}

