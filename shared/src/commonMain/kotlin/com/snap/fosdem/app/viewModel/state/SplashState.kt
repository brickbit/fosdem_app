package com.snap.fosdem.app.viewModel.state

sealed class SplashState {
    data object Init: SplashState()
    data object Finished: SplashState()
}