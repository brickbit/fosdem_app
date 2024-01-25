package com.rgr.fosdem.data.model.dto

import com.rgr.fosdem.domain.model.TrackBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("events")
        val events: List<EventDto>,
        @SerialName("stands")
        val stands: List<StandDto>,
)

fun TrackDto.toBo() = TrackBo(
        id = id,
        name = name,
        events = events.map { it.toBo() },
        stands = stands.map { it.toBo() }
)
