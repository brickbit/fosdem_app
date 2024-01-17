package com.snap.fosdem.android.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

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

