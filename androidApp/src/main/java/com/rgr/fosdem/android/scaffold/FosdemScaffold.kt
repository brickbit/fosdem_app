package com.rgr.fosdem.android.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.rgr.fosdem.android.R
import com.rgr.fosdem.android.navigation.Navigator
import com.rgr.fosdem.android.screens.common.MainTopBar
import com.rgr.fosdem.android.screens.common.TitleTopBar
import com.rgr.fosdem.app.navigation.Routes

@Composable
fun FosdemScaffold(
    navController: NavHostController,
    visible: Boolean,
    route: Routes
) {
    Scaffold(
        topBar = {
            if (visible) {
                when(route) {
                    Routes.Main ->  MainTopBar(
                        navigateToSettings = { navController.navigate(Routes.Settings.name) }
                    )
                    Routes.Settings ->  TitleTopBar(stringResource(R.string.settings))
                    Routes.Talk ->  Box(modifier = Modifier)
                    Routes.Language ->  TitleTopBar(stringResource(R.string.settings_language))
                    Routes.WebView -> TitleTopBar(stringResource(R.string.settings_app_license))
                    Routes.ThirdPartyLibraries -> TitleTopBar(stringResource(R.string.settings_licenses))
                    else -> {}
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            Navigator(navController = navController)
        }
    }
}