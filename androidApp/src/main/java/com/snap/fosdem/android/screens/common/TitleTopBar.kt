package com.snap.fosdem.android.screens.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopBar() {
    TopAppBar(title = {
        Text(
            text = "Ajustes",
            style = MaterialTheme.typography.titleMedium
        )
    })
}