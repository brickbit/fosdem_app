package com.snap.fosdem.app.state

import com.snap.fosdem.domain.model.EventBo

sealed class ScheduleState {
    data object Loading: ScheduleState()
    data class Loaded(
        val filter: ScheduleFilter
    ): ScheduleState()
    data class Empty(
        val filter: ScheduleFilter
    ): ScheduleState()
}

data class ScheduleFilter(
    val day: String = "Saturday",
    val hours: List<String> = emptyList(),
    val room: String = "",
    val track: String = "",
    val events: List<EventBo> = emptyList(),
)