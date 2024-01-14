package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackBo(
        val id: String,
        val name: String,
        val events: List<EventBo>,
        val checked: Boolean = false,
)
