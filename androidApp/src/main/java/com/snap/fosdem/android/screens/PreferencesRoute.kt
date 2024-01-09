package com.snap.fosdem.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.snap.fosdem.app.viewModel.PreferencesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreferencesRoute(
    viewModel: PreferencesViewModel = koinViewModel(),
    onNavigate: () -> Unit
) {
    Column {
        Text(text = "Preferences")
        Button(onClick = { onNavigate() }) {
            Text(text = "Next")
        }
    }
}