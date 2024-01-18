package com.snap.fosdem.android.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.snap.fosdem.android.BuildConfig
import com.snap.fosdem.android.MainActivity
import com.snap.fosdem.android.R
import com.snap.fosdem.android.screens.common.GrantPermission
import com.snap.fosdem.app.viewModel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToLanguage: () -> Unit,
    navigateToPreferences: () -> Unit,
    navigateToAbout: (String) -> Unit,
) {
    val stateNotifications = viewModel.stateNotificationEnabled.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.checkNotifications()
    }
    if(!stateNotifications) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            GrantPermission(
                permission = Manifest.permission.POST_NOTIFICATIONS,
                onPermissionGranted = { viewModel.changeNotificationStatus(it) },
            )
        } else {
            GrantPermission(
                permission = Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                onPermissionGranted = { viewModel.changeNotificationStatus(it) },
            )
        }
    }
    SettingsScreen(
        stateNotifications = stateNotifications,
        navigateToLanguage = navigateToLanguage,
        navigateToPreferences = navigateToPreferences,
        navigateToAbout = navigateToAbout,
    )
}

@Composable
fun SettingsScreen(
    stateNotifications: Boolean,
    navigateToLanguage: () -> Unit,
    navigateToPreferences: () -> Unit,
    navigateToAbout: (String) -> Unit
) {
    val context = LocalContext.current as MainActivity

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            SettingItem(
                name = stringResource(id = R.string.language),
                onNavigate = { navigateToLanguage() }
            )
            SettingItemWithSwitch(
                name = stringResource(R.string.notifications),
                notificationsEnabled = stateNotifications,
                onNotificationsChange = { granted ->
                    if(granted) {

                    } else {
                        val intent = Intent(
                            ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.packageManager)
                        )
                        context.startActivity(intent)


                    }
                }
            )
            SettingItemWithSubtitle(
                name = "Aviso de notificación",
                subtitle = "10 minutos antes",
                onNavigate = { navigateToLanguage() }
            )
            SettingItem(
                name = stringResource(R.string.preferred_tracks),
                onNavigate = { navigateToPreferences() }
            )
            SettingItem(
                name = stringResource(R.string.settings_licenses),
                onNavigate = {
                }
            )
            SettingItem(
                name = stringResource(R.string.settings_app_license),
                onNavigate = {
                    navigateToAbout(BuildConfig.license)
                }
            )
        }
        SettingsFooter()
    }

}

@Composable
fun SettingsFooter() {
    val context = LocalContext.current
    Column {
        val annotatedString = buildAnnotatedString {
            append(stringResource(R.string.settings_developed_by))
            pushStringAnnotation(tag = stringResource(R.string.settings_developer_tag), annotation = BuildConfig.linkedIn)
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(stringResource(R.string.settings_developer))
            }
            pop()
            append(stringResource(R.string.settings_source_code))
            pushStringAnnotation(tag = stringResource(R.string.settings_github_tag), annotation = BuildConfig.gitHub)
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(stringResource(R.string.settings_github))
            }
            pop()
        }

        ClickableText(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray, textAlign = TextAlign.Center),
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = context.getString(R.string.settings_developer_tag),
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(browserIntent)
                }
                annotatedString.getStringAnnotations(
                    tag = context.getString(R.string.settings_github_tag),
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(browserIntent)
                }
            })
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(R.string.settings_version, BuildConfig.VERSION_NAME),
            style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray),
            textAlign = TextAlign.Center
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
fun SettingItemWithSubtitle(
    name: String,
    subtitle: String,
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
        supportingContent = {
            Text(
                text = subtitle,
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