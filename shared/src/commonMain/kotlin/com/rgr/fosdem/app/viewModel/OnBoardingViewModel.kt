package com.rgr.fosdem.app.viewModel

import com.rgr.fosdem.app.flow.toCommonStateFlow
import com.rgr.fosdem.app.state.OnBoardingState
import com.rgr.fosdem.app.state.PreferencesState
import com.rgr.fosdem.domain.useCase.SaveOnBoardingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val useCase: SaveOnBoardingUseCase
): BaseViewModel() {

    private val _state: MutableStateFlow<OnBoardingState> = MutableStateFlow(OnBoardingState.Init)
    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OnBoardingState.Init
    ).toCommonStateFlow()

    fun saveOnBoarding() {
        scope.launch {
            useCase.invoke().onSuccess {
                _state.update {
                    OnBoardingState.Finished
                }
            }
        }
    }
}