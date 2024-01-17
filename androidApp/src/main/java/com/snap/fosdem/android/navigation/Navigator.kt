package com.snap.fosdem.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snap.fosdem.android.screens.LanguageRoute
import com.snap.fosdem.android.screens.MainRoute
import com.snap.fosdem.android.screens.OnBoardingRoute
import com.snap.fosdem.android.screens.PreferencesRoute
import com.snap.fosdem.android.screens.ScheduleRoute
import com.snap.fosdem.android.screens.SettingsRoute
import com.snap.fosdem.android.screens.SplashRoute
import com.snap.fosdem.android.screens.TalkRoute
import com.snap.fosdem.app.navigation.Routes

@Composable
fun Navigator(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.name
    ) {
        composable(Routes.Splash.name) {
            SplashRoute(
                onNavigate = { route ->
                    navController.navigate(route.name)
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
                onNavigate = { id ->
                    navController.navigate(Routes.Talk.goToDetail(id))
                },
                navigateToSchedule = {
                    navController.navigate(Routes.Schedule.name)
                }
            )
        }
        composable(
            route = Routes.Talk.name,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            TalkRoute(
                id = backStackEntry.arguments?.getString("id") ?: ""
            )
        }
        composable(Routes.Settings.name) {
            SettingsRoute(
                navigateToLanguage = { navController.navigate(Routes.Language.name) },
                navigateToPreferences = { navController.navigate(Routes.Preferences.name) }
            )
        }
        composable(Routes.Schedule.name) {
            ScheduleRoute()
        }
        composable(Routes.Language.name) {
            LanguageRoute(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}