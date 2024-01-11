package com.snap.fosdem.app.state

import com.snap.fosdem.app.navigation.Routes

sealed class SplashState {
    data object Init: SplashState()
    data class Finished(val route: Routes): SplashState()
    data object Error: SplashState()
}