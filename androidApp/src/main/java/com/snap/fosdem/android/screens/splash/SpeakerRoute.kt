package com.snap.fosdem.android.screens.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.snap.fosdem.app.viewModel.SpeakerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpeakerRoute(
    viewModel: SpeakerViewModel = koinViewModel()
) {
    Column {
        Text(text = "Speaker")
    }
}