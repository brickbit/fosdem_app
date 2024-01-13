package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackBo(
        val name: String,
        val events: List<EventBo>,
        val checked: Boolean = false,
)
