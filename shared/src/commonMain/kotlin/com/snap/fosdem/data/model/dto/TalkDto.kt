package com.snap.fosdem.data.model.dto

import com.snap.fosdem.domain.model.TalkBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TalkDto(
    @SerialName("id")
        val id: String,
    @SerialName("title")
        val title: String,
    @SerialName("description")
        val description: String,
    @SerialName("track")
        val track: String,
    @SerialName("room")
        val room: RoomDto,
    @SerialName("day")
        val day: String,
    @SerialName("start")
        val start: String,
    @SerialName("end")
        val end: String
)

fun TalkDto.toBo() = TalkBo(
        id = id,
        title = title,
        description = description,
        track = track,
        room = room.toBo(),
        day = day,
        start = start,
        end = end
)
