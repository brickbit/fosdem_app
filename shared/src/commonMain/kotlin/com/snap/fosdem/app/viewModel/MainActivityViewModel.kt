package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.navigation.Routes
import com.snap.fosdem.app.state.ScaffoldState
import com.snap.fosdem.app.state.SendNotificationState
import com.snap.fosdem.domain.useCase.GetEventsForNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getEventsForNotification: GetEventsForNotificationUseCase
): BaseViewModel() {

    private val _state: MutableStateFlow<ScaffoldState> = MutableStateFlow(ScaffoldState.Initialized(visible = false, route = Routes.Splash))
    val state = _state.asStateFlow()
    private val _sendNotificationState: MutableStateFlow<SendNotificationState> = MutableStateFlow(SendNotificationState.Initialized)
    val sendNotificationState = _sendNotificationState.asStateFlow()
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
            Routes.FavouriteTracks.name -> Routes.FavouriteTracks
            Routes.Settings.name -> Routes.Settings
            Routes.Splash.name -> Routes.Splash
            Routes.Talk.name -> Routes.Talk
            Routes.Schedule.name -> Routes.Schedule
            Routes.Language.name -> Routes.Language
            Routes.WebView.name -> Routes.WebView
            else -> null
        }
    }

    private fun shouldShowTopBar(route: Routes?): Boolean {
        return when(route) {
            Routes.Main -> true
            Routes.OnBoarding -> false
            Routes.FavouriteTracks -> false
            Routes.Settings -> true
            Routes.Splash -> false
            Routes.Talk -> true
            Routes.Schedule -> false
            Routes.Language -> true
            Routes.WebView -> true
            else -> false
        }
    }

    fun getEventsForNotification() {
        scope.launch {
            getEventsForNotification.invoke()
                .onSuccess { notification ->
                    _sendNotificationState.update {
                        SendNotificationState.Ready(notification)
                    }
                }
        }
    }
}