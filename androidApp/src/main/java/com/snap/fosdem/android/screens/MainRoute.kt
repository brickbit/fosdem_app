package com.snap.fosdem.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.snap.fosdem.android.screens.common.LoadingScreen
import com.snap.fosdem.app.state.MainState
import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.domain.model.TrackBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoute(
    viewModel: MainViewModel = koinViewModel(),
    onNavigate: () -> Unit
) {
    val state = viewModel.state.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.getPreferredTracks()
    }

    when(state) {
        is MainState.Loaded -> {
            MainScreen(
                tracks = state.tracks,
                onNavigate = onNavigate
            )
        }
        MainState.Loading -> LoadingScreen()
    }

}

@Composable
fun MainScreen(
    tracks: List<TrackBo>,
    onNavigate: () -> Unit
) {
    Column {
        Text(text = "Main")
        LazyColumn {
            items(tracks) { track ->
                Text(text = track.name)
            }
        }
        Button(onClick = { onNavigate() }) {
            Text(text = "Next")
        }
    }
}