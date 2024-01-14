package com.snap.fosdem.app.navigation

sealed class Routes(val name: String) {
    data object Splash: Routes("Splash")
    data object OnBoarding: Routes("OnBoarding")
    data object Preferences: Routes("Preferences")
    data object Main: Routes("Main")
    data object Talk: Routes("TalkRoute") {
        fun goToDetail(id: String): String = "TalkRoute/$id"
    }
    data object Settings: Routes("Settings")
    data object Schedule: Routes("Schedule")

}