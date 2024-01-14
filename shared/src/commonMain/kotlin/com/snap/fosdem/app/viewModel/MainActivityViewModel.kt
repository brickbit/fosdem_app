package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.navigation.Routes
import com.snap.fosdem.app.state.ScaffoldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivityViewModel: BaseViewModel() {

    private val _state: MutableStateFlow<ScaffoldState> = MutableStateFlow(ScaffoldState.Initialized(visible = false, route = Routes.Splash))
    val state = _state.asStateFlow()

    fun getRouteInformation(name: String?) {
        val route = getRouteByName(name)
        val visibility = shouldShowTopBar(route)
        route?.let {
            _state.update {
                ScaffoldState.Initialized(
                    visible = visibility,
                    route = route
                )
            }
        }
    }
    private fun getRouteByName(name: String?): Routes? {
        return when(name) {
            Routes.Main.name -> Routes.Main
            Routes.OnBoarding.name -> Routes.OnBoarding
            Routes.Preferences.name -> Routes.Preferences
            Routes.Settings.name -> Routes.Settings
            Routes.Splash.name -> Routes.Splash
            Routes.Talk.name -> Routes.Talk
            else -> null
        }
    }

    private fun shouldShowTopBar(route: Routes?): Boolean {
        return when(route) {
            Routes.Main -> true
            Routes.OnBoarding -> false
            Routes.Preferences -> true
            Routes.Settings -> true
            Routes.Splash -> false
            Routes.Talk -> true
            else -> false
        }
    }
}