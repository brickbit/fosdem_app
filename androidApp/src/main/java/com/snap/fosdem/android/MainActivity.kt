package com.snap.fosdem.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.snap.fosdem.android.scaffold.FosdemScaffold
import com.snap.fosdem.app.state.ScaffoldState
import com.snap.fosdem.app.viewModel.MainActivityViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController: NavHostController = rememberNavController()

            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val state = viewModel.state.collectAsState().value as ScaffoldState.Initialized
                    val routeName = navBackStackEntry?.destination?.route
                    LaunchedEffect(routeName) {
                        viewModel.getRouteInformation(routeName)
                    }
                    FosdemScaffold(
                        navController = navController,
                        visible = state.visible,
                        route = state.route
                    )
                }
            }
        }
    }
}



