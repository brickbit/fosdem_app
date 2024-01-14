package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TalkBo(
        val id: String,
        val title: String,
        val description: String,
        val track: String,
        val room: RoomBo,
        val day: String,
        val start: String,
        val end: String
)
