package com.rgr.fosdem.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgr.fosdem.domain.useCase.GetNotificationTimeUseCase
import com.rgr.fosdem.domain.useCase.GetNotificationsEnabledUseCase
import com.rgr.fosdem.domain.useCase.ManageNotificationPermissionUseCase
import com.rgr.fosdem.domain.useCase.ManageNotificationTimeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val notificationsEnabled: GetNotificationsEnabledUseCase,
    private val manageNotificationPermission: ManageNotificationPermissionUseCase,
    private val manageNotificationTime: ManageNotificationTimeUseCase,
    private val getNotificationTime: GetNotificationTimeUseCase
): ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private fun checkNotifications() {
        viewModelScope.launch {
            _state.update {
                val enabled = notificationsEnabled.invoke()
                it.copy(enabled = enabled, time = state.value.time)
            }
        }
    }

    fun changeNotificationStatus(status: Boolean) {
        viewModelScope.launch {
            manageNotificationPermission.invoke(status)
                .onSuccess {
                    checkNotifications()
                }
        }
    }

    fun selectNotificationTime(time: Int) {
        viewModelScope.launch {
            manageNotificationTime.invoke(time)
                .onSuccess {
                    getNotificationTime()
                }
        }
    }

    fun getNotificationTime() {
        viewModelScope.launch {
            val time = getNotificationTime.invoke()
            _state.update {
                it.copy(enabled = state.value.enabled, time = time)
            }
        }
    }
}

data class SettingsState(
    val enabled: Boolean = false,
    val time: Int = 10
)