package com.snap.fosdem.app.viewModel

import com.snap.fosdem.app.flow.toCommonStateFlow
import com.snap.fosdem.domain.useCase.GetNotificationsEnabledUseCase
import com.snap.fosdem.domain.useCase.ManageNotificationPermissionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val notificationsEnabled: GetNotificationsEnabledUseCase,
    private val manageNotificationPermission: ManageNotificationPermissionUseCase
): BaseViewModel() {
    private val _stateNotificationEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val stateNotificationEnabled = _stateNotificationEnabled.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    ).toCommonStateFlow()

    fun checkNotifications() {
        scope.launch {
            _stateNotificationEnabled.update {
                notificationsEnabled.invoke()
            }
        }
    }

    fun changeNotificationStatus(status: Boolean) {
        scope.launch {
            manageNotificationPermission.invoke(status)
        }
    }
}