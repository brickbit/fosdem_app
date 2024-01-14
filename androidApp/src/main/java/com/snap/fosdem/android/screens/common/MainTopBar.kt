package com.snap.fosdem.android.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.MyApplicationTheme
import com.snap.fosdem.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar() {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), 
                    contentDescription = null
                )
                Text(
                    text = "Fosdem",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        actions = {
            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(40.dp),
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = null
            )
        }
    )
}

@Preview(device = Devices.PIXEL_3A)
@Composable
fun MainTopBarPreview() {
    MyApplicationTheme {
        MainTopBar()
    }
}