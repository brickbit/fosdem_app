package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.TalkState
import com.snap.fosdem.domain.model.EventBo
import com.snap.fosdem.domain.useCase.GetEventByIdUseCase
import com.snap.fosdem.domain.useCase.IsEventNotifiedUseCase
import com.snap.fosdem.domain.useCase.ManageEventNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TalkViewModel(
    private val getEventById: GetEventByIdUseCase,
    private val manageEventNotification: ManageEventNotificationUseCase,
    private val isEventNotified: IsEventNotifiedUseCase
): BaseViewModel() {
    private val _state: MutableStateFlow<TalkState> = MutableStateFlow(TalkState.Loading)
    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TalkState.Loading
    ).toCommonStateFlow()
    private val _stateNotified: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val stateNotified = _stateNotified.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    ).toCommonStateFlow()

    fun getEvent(id: String) {
        scope.launch {
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
        scope.launch {
            manageEventNotification.invoke(event = event, enable = false)
            _stateNotified.update {
                isEventNotified.invoke(event)
            }
        }
    }

    fun disableEventNotification(event: EventBo) {
        scope.launch {
            manageEventNotification.invoke(event = event, enable = true)
            _stateNotified.update {
                isEventNotified.invoke(event)
            }
        }
    }

    fun isEventNotified(event: EventBo) {
        scope.launch {
            _stateNotified.update {
                isEventNotified.invoke(event)
            }
        }
    }
}