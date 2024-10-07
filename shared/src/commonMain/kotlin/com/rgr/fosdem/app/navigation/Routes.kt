package com.rgr.fosdem.app.navigation

sealed class Routes(val name: String) {
    data object Splash: Routes("Splash")
    data object OnBoarding: Routes("OnBoarding")
    data object FavouriteTracks: Routes("FavouritesTracks/{route}") {
        fun goFromRoute(route: String): String = "FavouritesTracks/$route"

    }
    data object Main: Routes("Main")
    data object Talk: Routes("TalkRoute/{id}") {
        fun goToDetail(id: String): String = "TalkRoute/$id"
    }
    data object Video: Routes("Video")
    data object Settings: Routes("Settings")
    data object Schedule: Routes("Schedule")
    data object Language: Routes("Language")
    data object WebView: Routes("WebView/{url}") {
        fun loadWebView(url: String): String = "WebView/$url"
    }
    data object ThirdPartyLibraries: Routes("ThirdLibraries")

    data object ListEvents: Routes("ListEvents/{title}/{eventType}") {
        fun goToDetail(title: String, eventType: String): String = "ListEvents/$title/$eventType"
    }
}