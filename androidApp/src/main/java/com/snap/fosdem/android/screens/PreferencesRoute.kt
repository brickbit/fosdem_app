package com.snap.fosdem.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.MyApplicationTheme
import com.snap.fosdem.android.screens.common.LoadingScreen
import com.snap.fosdem.app.state.PreferencesState
import com.snap.fosdem.app.viewModel.PreferencesViewModel
import com.snap.fosdem.domain.model.TrackBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreferencesRoute(
    viewModel: PreferencesViewModel = koinViewModel(),
    onNavigate: () -> Unit
) {
    val state = viewModel.state.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.getPreferences()
    }

    when(state) {
        PreferencesState.Error -> Text("Error")
        is PreferencesState.Loaded -> {
            PreferenceScreen(
                tracks = state.tracks,
                onTackChecked = { track, checked -> viewModel.onTrackChecked(track, checked) },
                enableContinueButton = { viewModel.enableContinueButton(it) },
                onContinueButtonClicked = {
                    viewModel.savePreferredTracks(state.tracks)
                }
            )
        }
        PreferencesState.Loading -> {
            LoadingScreen()
        }
        PreferencesState.Saved -> {
            LaunchedEffect(Unit) {
                onNavigate()
            }
        }
    }

}

@Composable
fun PreferenceScreen(
    tracks: List<TrackBo>,
    onTackChecked: (TrackBo, Boolean) -> Unit,
    enableContinueButton: (List<TrackBo>) -> Boolean,
    onContinueButtonClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(tracks) { track ->
                ListItem(
                    modifier = Modifier.padding(end = 16.dp),
                    leadingContent = {
                        Checkbox(
                            checked = track.checked,
                            onCheckedChange = { onTackChecked(track, it) })
                    },
                    headlineContent = {
                        Text(
                            text = track.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Button(
                enabled = enableContinueButton(tracks),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { onContinueButtonClicked() }
            ) {
                Text(text = "Continuar")
            }
        }
    }
}

@Preview(
    device = Devices.PIXEL_3A,
    backgroundColor = 0xFFFFFF)
@Composable
fun PreferenceScreenPreview() {
    MyApplicationTheme {
        PreferenceScreen(
            tracks = listOf(
                TrackBo(id = "1", name = "Android, iOS, Flutter", events = emptyList()),
                TrackBo(id = "2", name = "Vue, Angular", events = emptyList()),
                TrackBo(id = "3", name = "Spring Boot", events = emptyList()),
                TrackBo(id = "4", name = "AI", events = emptyList()),
            ),
            onTackChecked = {_,_ ->},
            enableContinueButton = { true },
            onContinueButtonClicked = {}
        )
    }
}