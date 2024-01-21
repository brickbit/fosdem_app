package com.snap.fosdem.data.model

import com.snap.fosdem.domain.model.EventBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
        @SerialName("id")
        val id: String,
        @SerialName("day")
        val day: String,
        @SerialName("talk")
        val talk: TalkDto,
        @SerialName("speaker")
        val speaker: List<SpeakerDto>,
        @SerialName("startHour")
        val startHour: String,
        @SerialName("endHour")
        val endHour: String,
        @SerialName("color")
        val color: String?,
)

fun EventDto.toBo(): EventBo = EventBo(
        id = id,
        day = day,
        talk = talk.toBo(),
        speaker = speaker.map{ it.toBo() },
        startHour = startHour,
        endHour = endHour,
        color = color
)
