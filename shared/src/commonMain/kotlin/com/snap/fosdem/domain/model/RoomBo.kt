package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RoomBo(
        val id: String,
        val name: String,
        val capacity: String,
        val building: BuildingBo,
        val location: String,
        val video: String,
        val chat: String,
)
