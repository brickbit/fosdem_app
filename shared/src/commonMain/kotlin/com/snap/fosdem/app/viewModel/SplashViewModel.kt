package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.navigation.Routes
import com.snap.fosdem.app.state.SplashState
import com.snap.fosdem.domain.provider.PermissionRepository
import com.snap.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getSchedule: GetScheduleDataUseCase,
    private val getOnBoardingStatus: GetOnBoardingStatusUseCase,
    private val getPreferredTask: GetPreferredTracksUseCase,
    private val permissionRepository: PermissionRepository
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
                        val onBoardingShown = getOnBoardingStatus.invoke()
                        val listTracks = getPreferredTask.invoke()
                        val route = if(onBoardingShown && listTracks.isEmpty()) {
                            Routes.Preferences
                        } else if(onBoardingShown && listTracks.isNotEmpty()){
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
    }

    fun savePermissionState(granted: Boolean) {
        scope.launch {
            permissionRepository.grantNotificationPermission(granted)
        }
    }

    
}