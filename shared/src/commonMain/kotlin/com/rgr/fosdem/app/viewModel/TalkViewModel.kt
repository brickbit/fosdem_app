package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.app.state.TalkState
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
    private val _state: MutableStateFlow<TalkState> = MutableStateFlow(TalkState.Loading)
    val state = _state.asStateFlow()

    private val _stateNotified: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val stateNotified = _stateNotified.asStateFlow()

    fun getEvent(id: String) {
        viewModelScope.launch {
            getEventById.invoke(id)
                .onSuccess { event ->
                    _state.update {
                        TalkState.Loaded(event)
                    }
                }
                .onFailure {  }
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