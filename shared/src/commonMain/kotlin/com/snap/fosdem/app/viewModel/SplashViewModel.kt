package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.viewModel.state.SplashState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel: BaseViewModel() {

    private val _state: MutableStateFlow<SplashState> = MutableStateFlow(SplashState.Init)
    val state = _state.asStateFlow()

    fun initializeSplash() {
        scope.launch {
            delay(3000)
            _state.update {
                SplashState.Finished
            }
        }
    }
}