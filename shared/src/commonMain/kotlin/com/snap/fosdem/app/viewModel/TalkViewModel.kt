package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.PreferencesState
import com.snap.fosdem.app.state.TalkState
import com.snap.fosdem.domain.useCase.GetEventByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TalkViewModel(
    private val getEventById: GetEventByIdUseCase
): BaseViewModel() {
    private val _state: MutableStateFlow<TalkState> = MutableStateFlow(TalkState.Loading)
    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TalkState.Loading
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
}