package com.snap.fosdem.data.model

import com.snap.fosdem.domain.model.TrackBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("events")
        val events: List<EventDto>
)

fun TrackDto.toBo() = TrackBo(
        id = id,
        name = name,
        events = events.map { it.toBo() }
)
