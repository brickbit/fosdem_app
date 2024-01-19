package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.NotificationBo

sealed class SendNotificationState {
    data object Initialized: SendNotificationState()
    data class Ready(val notification: NotificationBo): SendNotificationState()
}