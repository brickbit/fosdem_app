package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.SplashState
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getSchedule: GetScheduleDataUseCase
): BaseViewModel() {

    private val _state: MutableStateFlow<SplashState> = MutableStateFlow(SplashState.Init)
    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SplashState.Init
    ).toCommonStateFlow()

    fun initializeSplash() {
        scope.launch {
            delay(2000)
            getSchedule.invoke()
                .onSuccess {
                    _state.update {
                        SplashState.Finished
                    }
                }
                .onFailure {
                    SplashState.Error
                }
        }
    }
}