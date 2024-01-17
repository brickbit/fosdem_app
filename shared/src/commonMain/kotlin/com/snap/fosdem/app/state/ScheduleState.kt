package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.EventBo

sealed class ScheduleState {
    data object Loading: ScheduleState()
    data class Loaded(
        val day: String = "Saturday",
        val hours: List<String> = emptyList(),
        val rooms: List<String> = emptyList(),
        val tracks: List<String> = emptyList(),
        val events: List<EventBo> = emptyList(),
    ): ScheduleState()
}