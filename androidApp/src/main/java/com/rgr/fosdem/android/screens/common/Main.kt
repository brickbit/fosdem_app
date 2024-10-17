package com.rgr.fosdem.android.screens.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rgr.fosdem.android.MyApplicationTheme
import com.rgr.fosdem.android.scaffold.MainRoute
import com.rgr.fosdem.app.viewModel.MainActivityViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Main(
    navController: NavHostController,
    viewModel: MainActivityViewModel = koinViewModel()
) {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val state = viewModel.state.collectAsState().value
            val routeName = navBackStackEntry?.destination?.route

            LaunchedEffect(routeName) {
                viewModel.getRouteInformation(routeName)
            }
            MainRoute(
                navController = navController,
                visible = state.visible,
                route = state.route
            )
        }
    }
}