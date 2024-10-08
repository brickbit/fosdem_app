package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.useCase.GetFavouriteSchedulesUseCase
import com.rgr.fosdem.domain.useCase.GetRightNowSchedulesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val favouriteUseCase: GetFavouriteSchedulesUseCase,
    private val rightNowUseCase: GetRightNowSchedulesUseCase
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getFavouriteSchedules()
        getRightNowSchedules()
    }

    fun getFavouriteSchedules() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val schedules = favouriteUseCase.invoke()
            schedules.getOrNull()?.let { favourites ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        favouriteSchedules = favourites
                    )
                }
            } ?: handleError()
        }
    }

    fun getRightNowSchedules() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val schedules = rightNowUseCase.invoke()
            schedules.getOrNull()?.let { schedulesNow ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        rightNowSchedules = schedulesNow
                    )
                }
            } ?: handleError()
        }
    }

    private fun handleError() {

    }

}

data class HomeState(
    val isLoading: Boolean = true,
    val favouriteSchedules: List<ScheduleBo> = emptyList(),
    val rightNowSchedules: List<ScheduleBo> = emptyList(),
    val speakers: List<SpeakerBo> = emptyList(),
    val stands: List<StandBo> = emptyList()
)