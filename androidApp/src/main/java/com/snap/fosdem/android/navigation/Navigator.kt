package com.snap.fosdem.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.snap.fosdem.android.screens.splash.MainRoute
import com.snap.fosdem.android.screens.splash.OnBoardingRoute
import com.snap.fosdem.android.screens.splash.PreferencesRoute
import com.snap.fosdem.android.screens.splash.SpeakerRoute
import com.snap.fosdem.android.screens.splash.SplashRoute
import com.snap.fosdem.android.screens.splash.TalkRoute
import com.snap.fosdem.app.navigation.Routes

@Composable
fun Navigator() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.name
    ) {
        composable(Routes.Splash.name) {
            SplashRoute(
                onNavigate = {
                    navController.navigate(Routes.OnBoarding.name)
                }
            )
        }
        composable(Routes.OnBoarding.name) {
            OnBoardingRoute(
                onNavigate = {
                    navController.navigate(Routes.Preferences.name)
                }
            )
        }
        composable(Routes.Preferences.name) {
            PreferencesRoute(
                onNavigate = {
                    navController.navigate(Routes.Main.name)
                }
            )
        }
        composable(Routes.Main.name) {
            MainRoute(
                onNavigate = {
                    navController.navigate(Routes.Talk.name)
                }
            )
        }
        composable(Routes.Talk.name) {
            TalkRoute(
                onNavigate = {
                    navController.navigate(Routes.Speaker.name)
                }
            )
        }
        composable(Routes.Speaker.name) {
            SpeakerRoute()
        }
    }
}