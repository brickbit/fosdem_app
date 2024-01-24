package com.snap.fosdem.android.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.MyApplicationTheme
import com.snap.fosdem.android.R
import com.snap.fosdem.android.mainBrushColor
import com.snap.fosdem.android.screens.common.LoadingScreen
import com.snap.fosdem.android.transparentBrushColor
import com.snap.fosdem.android.transparentBrushColorReversed
import com.snap.fosdem.app.navigation.Routes
import com.snap.fosdem.app.state.PreferencesState
import com.snap.fosdem.app.viewModel.PreferencesViewModel
import com.snap.fosdem.domain.model.TrackBo
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreferencesRoute(
    viewModel: PreferencesViewModel = koinViewModel(),
    previousRoute: String,
    onNavigate: (String) -> Unit,
) {
    val state = viewModel.state.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.getPreferences()
    }

    when(state) {
        PreferencesState.Error -> Text(stringResource(R.string.favourite_error))
        is PreferencesState.Loaded -> {
            PreferenceScreen(
                previousRoute = previousRoute,
                tracks = state.tracks,
                onTackChecked = { track, checked -> viewModel.onTrackChecked(track, checked) },
                enableContinueButton = { viewModel.enableContinueButton(it) },
                onContinueButtonClicked = {
                    viewModel.savePreferredTracks(state.tracks)
                },
                onSkipClicked = {
                    viewModel.skipFavouriteTracks()
                    onNavigate(previousRoute)
                }
            )
        }
        PreferencesState.Loading -> {
            LoadingScreen()
        }
        PreferencesState.Saved -> {
            LaunchedEffect(Unit) {
                onNavigate(previousRoute)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceScreen(
    previousRoute: String,
    tracks: List<TrackBo>,
    onTackChecked: (TrackBo, Boolean) -> Unit,
    enableContinueButton: (List<TrackBo>) -> Boolean,
    onContinueButtonClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomCenter
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            stickyHeader {
                PreferenceTitle(
                    previousRoute = previousRoute,
                    onSkipClicked = onSkipClicked
                )
            }
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
            item{ Spacer(modifier = Modifier.padding(bottom = 90.dp)) }
        }
        PreferenceButton(
            tracks = tracks,
            enableContinueButton = enableContinueButton,
            onContinueButtonClicked = onContinueButtonClicked
        )
    }
}
@Composable
fun PreferenceTitle(
    previousRoute: String,
    onSkipClicked: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .height(130.dp)
            .background(Brush.verticalGradient(colorStops = transparentBrushColorReversed(context)))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.82f),
                text = stringResource(R.string.favourite_tracks_track),
                style = MaterialTheme.typography.titleMedium
            )
            if(previousRoute == Routes.Splash.name || previousRoute == Routes.OnBoarding.name) {
                Text(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(top = 8.dp)
                        .clickable{ onSkipClicked() },
                    text = stringResource(R.string.favourite_skip),
                    style = MaterialTheme.typography.titleSmall.copy(Color.Gray)
                )
            }
        }
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.favourite_tracks_track_subtitle),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PreferenceButton(
    tracks: List<TrackBo>,
    enableContinueButton: (List<TrackBo>) -> Boolean,
    onContinueButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(brush = Brush.verticalGradient(colorStops = transparentBrushColor(context))),
        contentAlignment = Alignment.BottomCenter
    ) {
        if(enableContinueButton(tracks)) {
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(colorStops = mainBrushColor),
                        shape = CircleShape
                    )
                    .clickable { onContinueButtonClicked() }
                    .padding(vertical = 16.dp, horizontal = 32.dp),
                text = stringResource(R.string.favourite_tracks_next_button),
                style = MaterialTheme.typography.titleSmall.copy(Color.White),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray,
                        shape = CircleShape
                    )
                    .padding(12.dp),
                text = stringResource(R.string.on_boarding_next_button),
                style = MaterialTheme.typography.titleSmall.copy(Color.DarkGray),
                textAlign = TextAlign.Center
            )
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
                TrackBo(id = "1", name = "Android, iOS, Flutter", events = emptyList(), stands = emptyList()),
                TrackBo(id = "2", name = "Vue, Angular", events = emptyList(), stands = emptyList()),
                TrackBo(id = "3", name = "Spring Boot", events = emptyList(), stands = emptyList()),
                TrackBo(id = "4", name = "AI", events = emptyList(), stands = emptyList()),
            ),
            onTackChecked = {_,_ ->},
            enableContinueButton = { true },
            onContinueButtonClicked = {},
            onSkipClicked = {},
            previousRoute = ""
        )
    }
}