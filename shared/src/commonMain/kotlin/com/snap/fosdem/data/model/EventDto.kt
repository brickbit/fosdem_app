package com.snap.fosdem.data.model

import com.snap.fosdem.domain.model.EventBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
        @SerialName("day")
        val day: String,
        @SerialName("talk")
        val talk: TalkDto?,
        @SerialName("speaker")
        val speaker: SpeakerDto?,
        @SerialName("startHour")
        val startHour: String,
        @SerialName("startHourLink")
        val startHourLink: String,
        @SerialName("endHour")
        val endHour: String,
        @SerialName("endHourLink")
        val endHourLink: String,
        @SerialName("color")
        val color: String?,
)

fun EventDto.toBo(): EventBo = EventBo(
        day = day,
        talk = talk?.toBo(),
        speaker = speaker?.toBo(),
        startHour = startHour,
        startHourLink = startHourLink,
        endHour = endHour,
        endHourLink = endHourLink,
        color = color
)
