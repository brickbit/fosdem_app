package com.snap.fosdem.domain.model

data class TalkBo(
        val title: String,
        val description: String,
        val track: String,
        val room: RoomBo,
        val day: String,
        val start: String,
        val end: String
)
