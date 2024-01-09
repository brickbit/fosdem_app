package com.snap.fosdem.app.navigation

sealed class Routes(val name: String) {
    data object Splash: Routes("Splash")
    data object OnBoarding: Routes("OnBoarding")
    data object Preferences: Routes("Preferences")
    data object Main: Routes("Main")
    data object Talk: Routes("TalkRoute")
    data object Speaker: Routes("SpeakerRoute")
}