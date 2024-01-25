package com.rgr.fosdem.app.state

import com.rgr.fosdem.app.navigation.Routes

sealed class SplashState {
    data object Init: SplashState()
    data class Finished(val route: Routes): SplashState()
    data object Error: SplashState()
}