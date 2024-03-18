package com.rgr.fosdem.app.state

sealed class OnBoardingState {
    data object Init: OnBoardingState()
    data object Finished: OnBoardingState()

}