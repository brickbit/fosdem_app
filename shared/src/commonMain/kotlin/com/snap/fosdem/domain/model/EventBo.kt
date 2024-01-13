package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EventBo(
        val day: String,
        val talk: TalkBo?,
        val speaker: SpeakerBo?,
        val startHour: String,
        val startHourLink: String,
        val endHour: String,
        val endHourLink: String,
        val color: String?,
)
