package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.navigation.Routes
import com.rgr.fosdem.domain.useCase.LoadSchedulesUseCase
import com.rgr.fosdem.domain.useCase.LoadSpeakersUseCase
import com.rgr.fosdem.domain.useCase.LoadStandsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val loadScheduleData: LoadSchedulesUseCase,
    private val loadStandsData: LoadStandsUseCase,
    private val loadSpeakersData: LoadSpeakersUseCase
): ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    fun initializeSplash() {
        viewModelScope.launch(dispatcher) {
            loadScheduleData.invoke()
                .onSuccess {
                    _state.update { it.copy(route = Routes.Main, isError = false) }
                }
                .onFailure {
                    _state.update { it.copy(isError = true) }
                }
            loadStandsData.invoke()
            loadSpeakersData.invoke()
            /*getSchedule.invoke()
                .onSuccess {
                    val onBoardingShown = getOnBoardingStatus.invoke()
                    val favouriteTracksShown = isFavouriteTracksShown.invoke()
                    val route = if(onBoardingShown && !favouriteTracksShown) {
                        Routes.FavouriteTracks
                    } else if(onBoardingShown && favouriteTracksShown){
                        Routes.Main
                    } else {
                        Routes.OnBoarding
                    }
                    _state.update {
                        it.copy(route = route)
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(isError = true)
                    }
                }*/

        }
    }
}

data class SplashState (
    val route: Routes? = null,
    val isError: Boolean = false
)