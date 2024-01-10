package com.snap.fosdem.data.model

import com.snap.fosdem.domain.model.BuildingBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildingDto(
        @SerialName("name")
        val name: String,
        @SerialName("online")
        val online: Boolean,
        @SerialName("map")
        val map: String,
)

fun BuildingDto.toBo() = BuildingBo(
        name = name,
        online = online,
        map = map
)