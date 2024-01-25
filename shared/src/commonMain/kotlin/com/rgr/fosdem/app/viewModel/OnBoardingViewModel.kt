package com.rgr.fosdem.app.viewModel

import com.rgr.fosdem.domain.useCase.SaveOnBoardingUseCase
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val useCase: SaveOnBoardingUseCase
): BaseViewModel() {

    fun saveOnBoarding() {
        scope.launch {
            useCase.invoke()
        }
    }
}