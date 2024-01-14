package com.snap.fosdem.android.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.snap.fosdem.android.navigation.Navigator
import com.snap.fosdem.android.screens.common.MainTopBar
import com.snap.fosdem.android.screens.common.TextTopBar
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
                    Routes.Main ->  MainTopBar()
                    Routes.Preferences ->  TextTopBar()
                    Routes.Speaker ->  Box(modifier = Modifier)
                    Routes.Talk ->  Box(modifier = Modifier)
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