package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.navigation.Routes
import com.snap.fosdem.app.state.SplashState
import com.snap.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.snap.fosdem.domain.useCase.GetPreferredTracksShownUseCase
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import com.snap.fosdem.domain.useCase.IsUpdateNeeded
import com.snap.fosdem.domain.useCase.ManageNotificationPermissionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val needUpdate: IsUpdateNeeded,
    private val getSchedule: GetScheduleDataUseCase,
    private val getOnBoardingStatus: GetOnBoardingStatusUseCase,
    private val isFavouriteTracksShown: GetPreferredTracksShownUseCase,
    private val manageNotificationPermission: ManageNotificationPermissionUseCase
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
            needUpdate.invoke()
                .onSuccess { shouldUpdate ->
                    getSchedule.invoke(shouldUpdate)
                        .onSuccess {
                            _state.update {
                                val onBoardingShown = getOnBoardingStatus.invoke()
                                val favouriteTracksShown = isFavouriteTracksShown.invoke()
                                val route = if(onBoardingShown && !favouriteTracksShown) {
                                    Routes.FavouriteTracks
                                } else if(onBoardingShown && favouriteTracksShown){
                                    Routes.Main
                                } else {
                                    Routes.OnBoarding
                                }
                                SplashState.Finished(route)
                            }
                        }
                        .onFailure {
                            SplashState.Error
                        }
                }
                .onFailure {  }

        }
    }

    fun saveNotificationPermissionState(granted: Boolean) {
        scope.launch {
            manageNotificationPermission.invoke(granted)
        }
    }


    
}