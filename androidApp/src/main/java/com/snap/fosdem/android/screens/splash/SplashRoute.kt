package com.snap.fosdem.android.screens.splash

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.state.SplashState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initializeSplash()
    }
    val state = viewModel.state.collectAsState().value
    SplashScreen(state = state)
}
@Composable
fun SplashScreen(
    state: SplashState
) {
    when(state) {
        SplashState.Finished -> Text(text = "Finished")
        SplashState.Init -> Text(text = "Initialized")
    }
}