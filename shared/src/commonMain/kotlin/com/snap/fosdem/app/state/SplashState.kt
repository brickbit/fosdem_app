package com.snap.fosdem.app.state

sealed class SplashState {
    data object Init: SplashState()
    data object Finished: SplashState()
    data object Error: SplashState()
}