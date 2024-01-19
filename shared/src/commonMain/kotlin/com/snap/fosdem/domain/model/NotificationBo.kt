package com.snap.fosdem.domain.model

data class NotificationBo(
    val events: List<EventBo>,
    val time: Int,
)
