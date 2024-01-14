package com.snap.fosdem.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.snap.fosdem.app.viewModel.TalkViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TalkRoute(
    id: String,
    viewModel: TalkViewModel = koinViewModel(),
) {
    Column {
        Text(text = "Talk $id")
        Button(onClick = {  }) {
            Text(text = "Next")
        }
    }
}