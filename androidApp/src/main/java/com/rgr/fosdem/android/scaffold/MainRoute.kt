package com.rgr.fosdem.android.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.rgr.fosdem.android.R
import com.rgr.fosdem.android.navigation.Navigator
import com.rgr.fosdem.android.screens.common.MainTopBar
import com.rgr.fosdem.android.screens.common.TitleTopBar
import com.rgr.fosdem.app.navigation.Routes

@Composable
fun MainRoute(
    navController: NavHostController,
    visible: Boolean,
    route: Routes
) {
    Scaffold(
        topBar = {
            if(visible) {
                when(route) {
                    Routes.Language -> {
                        TitleTopBar(stringResource(R.string.settings_language))
                    }
                    Routes.Main -> {
                        MainTopBar()
                    }
                    Routes.Schedule -> {
                        TitleTopBar("Schedule")
                    }
                    Routes.Settings -> {
                        TitleTopBar(stringResource(R.string.settings))
                    }
                    Routes.ThirdPartyLibraries -> {
                        TitleTopBar(stringResource(R.string.settings_licenses))
                    }
                    Routes.WebView -> {
                        TitleTopBar(stringResource(R.string.settings_app_license))
                    }
                    else -> {}
                }
            }
        },
        bottomBar = {
            when(route) {
                Routes.Splash, Routes.OnBoarding -> {}
                else -> {
                    BottomAppBar(
                        contentColor = Color.White,
                        containerColor = if(route.name == Routes.Main.name || route.name == Routes.Settings.name) { Color.White } else{ Color.Black }
                    ) {
                        MainBottomBar(
                            navController = navController,
                            route = route,
                            backgroundLight = route.name == Routes.Main.name || route.name == Routes.Settings.name
                        )
                    }
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

@Composable
fun MainBottomBar(
    navController: NavHostController,
    route: Routes,
    backgroundLight: Boolean
) {
    Row {
        BottomNavigationItem(
            selected = route.name == Routes.Main.name,
            onClick = { navController.navigate(Routes.Main.name) },
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Localized description",
                    tint = if (route.name == Routes.Main.name) Color(0xFFAB1B93) else { if(backgroundLight) Color.Gray else Color.White }
                )
            },
            selectedContentColor = Color(0xFFAB1B93),
            unselectedContentColor = Color.Blue
        )
        BottomNavigationItem(
            selected = route.name == Routes.Schedule.name,
            onClick = { navController.navigate(Routes.Schedule.name) },
            icon = {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Localized description",
                    tint = if (route.name == Routes.Schedule.name) Color(0xFFAB1B93) else { if(backgroundLight) Color.Gray else Color.White }
                )
            },
        )
        BottomNavigationItem(
            selected = route.name == Routes.Video.name,
            onClick = { navController.navigate(Routes.Video.name) },
            icon = {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "Localized description",
                    tint = if (route.name == Routes.Video.name) Color(0xFFAB1B93) else { if(backgroundLight) Color.Gray else Color.White }
                )
            },
        )
        BottomNavigationItem(
            selected = route.name == Routes.Settings.name,
            onClick = { navController.navigate(Routes.Settings.name) },
            icon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Localized description",
                    tint = if (route.name == Routes.Settings.name) Color(0xFFAB1B93) else { if(backgroundLight) Color.Gray else Color.White }
                )
            },
        )
    }
}