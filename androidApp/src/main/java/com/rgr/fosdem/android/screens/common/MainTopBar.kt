package com.rgr.fosdem.android.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rgr.fosdem.android.MyApplicationTheme
import com.rgr.fosdem.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    navigateToSettings: () -> Unit
) {
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
                    text = stringResource(id = R.string.top_bar_fosdem),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        actions = {
            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(30.dp)
                    .clickable { navigateToSettings() },
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        }
    )
}

@Preview(device = Devices.PIXEL_3A)
@Composable
fun MainTopBarPreview() {
    MyApplicationTheme {
        MainTopBar {}
    }
}