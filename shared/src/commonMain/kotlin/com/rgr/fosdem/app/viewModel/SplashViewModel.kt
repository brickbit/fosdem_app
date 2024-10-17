package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.navigation.Routes
import com.rgr.fosdem.domain.useCase.LoadSchedulesUseCase
import com.rgr.fosdem.domain.useCase.LoadSpeakersUseCase
import com.rgr.fosdem.domain.useCase.LoadStandsUseCase
import com.rgr.fosdem.domain.useCase.LoadVideosUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val loadScheduleData: LoadSchedulesUseCase,
    private val loadVideosData: LoadVideosUseCase,
    private val loadStandsData: LoadStandsUseCase,
    private val loadSpeakersData: LoadSpeakersUseCase
): ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    fun initializeSplash() {
        viewModelScope.launch(dispatcher) {
            val scheduleRequest = async { loadScheduleData.invoke() }
            val videoRequest = async { loadVideosData.invoke() }
            val standsRequest = async { loadStandsData.invoke() }
            //val speakersRequest = async { loadSpeakersData.invoke() }

            val scheduleLoaded = scheduleRequest.await()
            val videosLoaded = videoRequest.await()
            val standsLoaded = standsRequest.await()
            //val speakersLoaded = speakersRequest.await()

            if (scheduleLoaded.isSuccess && videosLoaded.isSuccess && standsLoaded.isSuccess /*&& speakersLoaded.isSuccess */) {
                _state.update { it.copy(route = Routes.Main, isError = false) }
            } else {
                _state.update { it.copy(isError = true) }
            }
        }
    }
}

data class SplashState (
    val route: Routes? = null,
    val isError: Boolean = false
)