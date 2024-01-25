package com.rgr.fosdem.data.model.dto

import com.rgr.fosdem.domain.model.RoomBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomDto(
    @SerialName("id")
        val id: String,
    @SerialName("name")
        val name: String,
    @SerialName("capacity")
        val capacity: String,
    @SerialName("building")
        val building: BuildingDto,
    @SerialName("location")
        val location: String,
    @SerialName("video")
        val video: String,
    @SerialName("chat")
        val chat: String,
)

fun RoomDto.toBo() = RoomBo(
        id = id,
        name = name,
        capacity = capacity,
        building = building.toBo(),
        location = location,
        video = video,
        chat = chat
)
