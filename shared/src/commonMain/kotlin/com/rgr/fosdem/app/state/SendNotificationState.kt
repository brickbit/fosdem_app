package com.rgr.fosdem.app.state

import com.rgr.fosdem.domain.model.NotificationBo

sealed class SendNotificationState {
    data object Initialized: SendNotificationState()
    data class Ready(val notification: NotificationBo): SendNotificationState()
}