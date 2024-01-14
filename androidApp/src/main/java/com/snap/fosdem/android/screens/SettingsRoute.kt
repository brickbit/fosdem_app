package com.snap.fosdem.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.snap.fosdem.android.R
import com.snap.fosdem.app.viewModel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = koinViewModel()
) {
    Column {
        SettingItem(
           name = "Idioma",
            onNavigate = { }
        )
        SettingItemWithSwitch(
            name = "Notificaciones",
            notificationsEnabled = false,
            onNotificationsChange = { }
        )
        SettingItem(
            name = "Tracks preferidos",
            onNavigate = { }
        )
        SettingItem(
            name = "Licencias",
            onNavigate = { }
        )
    }
}

@Composable
fun SettingItem(
    name: String,
    onNavigate: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onNavigate() },
        headlineContent = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null
            )
        }
    )
    Divider(color = Color(0xFFCCCCCC))
}

@Composable
fun SettingItemWithSwitch(
    name: String,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall
            )
        },
        trailingContent = {
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { onNotificationsChange(it) }
            )
        }
    )
    Divider(color = Color(0xFFCCCCCC))
}