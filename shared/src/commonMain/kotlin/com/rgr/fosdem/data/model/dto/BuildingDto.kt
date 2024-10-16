package com.rgr.fosdem.data.model.dto

import com.rgr.fosdem.domain.model.BuildingBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildingDto(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("online")
        val online: Boolean,
        @SerialName("map")
        val map: String,
)

fun BuildingDto.toBo() = BuildingBo(
        id = id,
        name = name,
        online = online,
        map = map
)