package com.snap.fosdem.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.snap.fosdem.app.viewModel.TalkViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TalkRoute(
    viewModel: TalkViewModel = koinViewModel(),
    onNavigate: () -> Unit
) {
    Column {
        Text(text = "Talk")
        Button(onClick = { onNavigate() }) {
            Text(text = "Next")
        }
    }
}