package com.snap.fosdem.android.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextTopBar() {
    Column(
        modifier = Modifier
            .height(120.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tracks",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Selecciona los tracks en los que estés más interesado",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}