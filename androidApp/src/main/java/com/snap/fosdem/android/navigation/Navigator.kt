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
import com.snap.fosdem.android.screens.common.CustomWebView
import com.snap.fosdem.app.navigation.Routes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                    when(route) {
                        Routes.FavouriteTracks -> navController.navigate(Routes.FavouriteTracks.goFromRoute(Routes.Splash.name))
                        else -> navController.navigate(route.name)
                    }

                }
            )
        }
        composable(Routes.OnBoarding.name) {
            OnBoardingRoute(
                onNavigate = {
                    navController.navigate(Routes.FavouriteTracks.goFromRoute(Routes.OnBoarding.name))
                }
            )
        }
        composable(
            route = Routes.FavouriteTracks.name,
            arguments = listOf(navArgument("route") { type = NavType.StringType })
        ) { backStackEntry ->
            PreferencesRoute(
                previousRoute = backStackEntry.arguments?.getString("route") ?: "" ,
                onNavigate = { route ->
                    when(route) {
                        Routes.Settings.name -> navController.popBackStack()
                        else -> navController.navigate(Routes.Main.name)

                    }

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
                navigateToPreferences = {
                    navController.navigate(Routes.FavouriteTracks.goFromRoute(Routes.Settings.name))
                                        },
                navigateToAbout = { url ->
                    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.WebView.loadWebView(encodedUrl))
                }
            )
        }
        composable(Routes.Schedule.name) {
            ScheduleRoute(
                onEventClicked = { id ->
                    navController.navigate(Routes.Talk.goToDetail(id))
                }
            )
        }
        composable(Routes.Language.name) {
            LanguageRoute(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.WebView.name,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            CustomWebView(
                url = backStackEntry.arguments?.getString("url") ?: ""
            )
        }
    }
}