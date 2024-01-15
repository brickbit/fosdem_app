package com.snap.fosdem.android.scaffold

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.snap.fosdem.android.R
import com.snap.fosdem.android.navigation.Navigator
import com.snap.fosdem.android.screens.common.MainTopBar
import com.snap.fosdem.android.screens.common.TitleTopBar
import com.snap.fosdem.app.navigation.Routes

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
                    Routes.Schedule ->  TitleTopBar(stringResource(R.string.schedule))
                    Routes.Language ->  TitleTopBar(stringResource(R.string.language))
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