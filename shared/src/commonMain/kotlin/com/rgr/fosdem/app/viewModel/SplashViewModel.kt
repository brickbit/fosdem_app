package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.navigation.Routes
import com.rgr.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksShownUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleDataUseCase
import com.rgr.fosdem.domain.useCase.ManageNotificationPermissionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getSchedule: GetScheduleDataUseCase,
    private val getOnBoardingStatus: GetOnBoardingStatusUseCase,
    private val isFavouriteTracksShown: GetPreferredTracksShownUseCase,
    private val manageNotificationPermission: ManageNotificationPermissionUseCase
): ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    fun initializeSplash() {
        viewModelScope.launch(dispatcher) {
            getSchedule.invoke()
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
                }

        }
    }

    fun saveNotificationPermissionState(granted: Boolean) {
        viewModelScope.launch {
            manageNotificationPermission.invoke(granted)
        }
    }
}

data class SplashState (
    val route: Routes? = null,
    val isError: Boolean = false
)