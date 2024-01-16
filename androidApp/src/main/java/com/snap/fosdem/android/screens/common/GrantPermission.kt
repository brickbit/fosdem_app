package com.snap.fosdem.android.screens.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GrantPermission(
    permission: String,
    onPermissionGranted: (Boolean) -> Unit,
) {
    val permissionState = rememberPermissionState(permission)

    LaunchedEffect(permissionState) {
        val permissionResult = permissionState.status

        if (!permissionResult.isGranted) {
            if (permissionResult.shouldShowRationale) {
                // Show a rationale if needed (optional)
            } else {
                // Request the permission
                permissionState.launchPermissionRequest()
            }
        }
        onPermissionGranted (permissionResult.isGranted)
    }
}

