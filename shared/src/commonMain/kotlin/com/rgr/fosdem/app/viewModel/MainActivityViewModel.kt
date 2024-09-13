package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.navigation.Routes
import com.rgr.fosdem.domain.model.NotificationBo
import com.rgr.fosdem.domain.useCase.GetEventsForNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getEventsForNotification: GetEventsForNotificationUseCase
): ViewModel() {

    private val _state = MutableStateFlow(ScaffoldState())
    val state = _state.asStateFlow()

    fun getRouteInformation(name: String?) {
        val route = getRouteByName(name)
        val visibility = shouldShowTopBar(route)
        route?.let {
            _state.update {
                it.copy(
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
            Routes.ThirdPartyLibraries.name -> Routes.ThirdPartyLibraries
            Routes.ListEvents.name -> Routes.ListEvents
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
            Routes.WebView -> false
            Routes.ThirdPartyLibraries -> true
            Routes.ListEvents -> false
            else -> false
        }
    }

    fun getEventsForNotification() {
        viewModelScope.launch {
            getEventsForNotification.invoke()
                .onSuccess { notification ->
                    _state.update { it.copy(notification = notification) }
                }
        }
    }
}

data class ScaffoldState (
    val visible: Boolean = false,
    val route: Routes = Routes.Splash,
    val notification: NotificationBo? = null
)