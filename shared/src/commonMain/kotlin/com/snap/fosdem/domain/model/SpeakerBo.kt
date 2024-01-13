package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SpeakerBo(
        val name: String,
        val image: String?,
        val description: String?,
)
