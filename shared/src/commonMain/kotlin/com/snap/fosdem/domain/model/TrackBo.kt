package com.snap.fosdem.domain.model

data class TrackBo(
        val name: String,
        val events: List<EventBo>
)
