package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.app.state.NotificationState
import com.snap.fosdem.domain.useCase.GetNotificationTimeUseCase
import com.snap.fosdem.domain.useCase.GetNotificationsEnabledUseCase
import com.snap.fosdem.domain.useCase.ManageNotificationPermissionUseCase
import com.snap.fosdem.domain.useCase.ManageNotificationTimeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val notificationsEnabled: GetNotificationsEnabledUseCase,
    private val manageNotificationPermission: ManageNotificationPermissionUseCase,
    private val manageNotificationTime: ManageNotificationTimeUseCase,
    private val getNotificationTime: GetNotificationTimeUseCase
): BaseViewModel() {
    private val _state: MutableStateFlow<NotificationState> = MutableStateFlow(NotificationState())
    val state = _state.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotificationState()
    ).toCommonStateFlow()


    fun checkNotifications() {
        scope.launch {
            _state.update {
                val enabled = notificationsEnabled.invoke()
                it.copy(enabled = enabled, time = state.value.time)
            }
        }
    }

    fun changeNotificationStatus(status: Boolean) {
        scope.launch {
            manageNotificationPermission.invoke(status)
                .onSuccess {
                    checkNotifications()
                }
        }
    }

    fun selectNotificationTime(time: Int) {
        scope.launch {
            manageNotificationTime.invoke(time)
                .onSuccess {
                    getNotificationTime()
                }
        }
    }

    fun getNotificationTime() {
        scope.launch {
            val time = getNotificationTime.invoke()
            _state.update {
                it.copy(enabled = state.value.enabled, time = time)
            }
        }
    }
}