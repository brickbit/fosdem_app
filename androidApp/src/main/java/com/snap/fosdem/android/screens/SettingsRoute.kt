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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.snap.fosdem.android.BuildConfig
import com.snap.fosdem.android.R
import com.snap.fosdem.app.viewModel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToLanguage: () -> Unit,
    navigateToPreferences: () -> Unit,
    navigateToAbout: (String) -> Unit,
) {
    val state = viewModel.state.collectAsState().value
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val notificationPermissionState = rememberPermissionState(permission =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        Manifest.permission.ACCESS_NOTIFICATION_POLICY
    }
    )
    DisposableEffect(lifecycleOwner) {
        val lifecycleEventObserver = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.getNotificationTime()
                    viewModel.changeNotificationStatus(
                        NotificationManagerCompat.from(context).areNotificationsEnabled()
                    )
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)
        onDispose { lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver) }
    }


    SettingsScreen(
        enabled = state.enabled,
        time = state.time,
        onSelectNotificationTime = { viewModel.selectNotificationTime(it) },
        requestPermission = {
            notificationPermissionState.launchPermissionRequest()
        },
        withdrawPermission = {
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
        },
        navigateToLanguage = navigateToLanguage,
        navigateToPreferences = navigateToPreferences,
        navigateToAbout = navigateToAbout,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    enabled: Boolean,
    time: Int,
    onSelectNotificationTime: (Int) -> Unit,
    requestPermission: () -> Unit,
    withdrawPermission: () -> Unit,
    navigateToLanguage: () -> Unit,
    navigateToPreferences: () -> Unit,
    navigateToAbout: (String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

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
                notificationsEnabled = enabled,
                onNotificationsChange = { granted ->
                    if(!granted) {
                        withdrawPermission()
                    } else {
                        requestPermission()
                    }
                }
            )
            SettingItemWithSubtitle(
                name = stringResource(R.string.settings_notification_time),
                subtitle = stringResource(R.string.settings_notification_time_selected, time.toString()),
                onActionClicked = { showBottomSheet = true }
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

    if (showBottomSheet) {
        NotificationBottomSheet(
            sheetState = sheetState,
            onDismiss = { showBottomSheet = false },
            onAcceptDelay = {
                onSelectNotificationTime(it)
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }
        )
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
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        }
    )
    Divider(color = Color(0xFFCCCCCC))
}
@Composable
fun SettingItemWithSubtitle(
    name: String,
    subtitle: String,
    onActionClicked: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onActionClicked() },
        headlineContent = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAcceptDelay: (Int) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn {
                items(5){
                    ListItem(
                        modifier = Modifier.clickable {
                            onAcceptDelay((it+1)*10)
                        },
                        headlineContent = {
                            Text(text = "${(it+1)*10} minutos antes")
                        }
                    )
                    Divider()
                }
            }
        }
    }
}