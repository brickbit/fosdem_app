package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.state.OnBoardingState
import com.rgr.fosdem.domain.useCase.SaveOnBoardingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val useCase: SaveOnBoardingUseCase
): ViewModel() {

    private val _state: MutableStateFlow<OnBoardingState> = MutableStateFlow(OnBoardingState.Init)
    val state = _state.asStateFlow()

    fun saveOnBoarding() {
        viewModelScope.launch {
            useCase.invoke().onSuccess {
                _state.update {
                    OnBoardingState.Finished
                }
            }
        }
    }
}