package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.domain.model.EventBo
import com.rgr.fosdem.domain.useCase.GetEventByIdUseCase
import com.rgr.fosdem.domain.useCase.IsEventNotifiedUseCase
import com.rgr.fosdem.domain.useCase.ManageEventNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TalkViewModel(
    private val getEventById: GetEventByIdUseCase,
    private val manageEventNotification: ManageEventNotificationUseCase,
    private val isEventNotified: IsEventNotifiedUseCase
): ViewModel() {
    private val _state = MutableStateFlow(TalkState())
    val state = _state.asStateFlow()

    private val _stateNotified: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val stateNotified = _stateNotified.asStateFlow()

    fun getEvent(id: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getEventById.invoke(id)
                .onSuccess { event ->
                    _state.update { it.copy(isLoading = false, talkEvent = event) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun activateEventNotification(event: EventBo) {
        viewModelScope.launch {
            manageEventNotification.invoke(event = event, enable = false)
            _stateNotified.update {
                isEventNotified.invoke(event)
            }
        }
    }

    fun disableEventNotification(event: EventBo) {
        viewModelScope.launch {
            manageEventNotification.invoke(event = event, enable = true)
            _stateNotified.update {
                isEventNotified.invoke(event)
            }
        }
    }

    fun isEventNotified(event: EventBo) {
        viewModelScope.launch {
            _stateNotified.update {
                isEventNotified.invoke(event)
            }
        }
    }
}

data class TalkState (
    val isLoading: Boolean = false,
    val talkEvent: EventBo? = null
)