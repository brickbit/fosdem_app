package com.rgr.fosdem.domain.model.bo

data class VideoBo(
    val idTalk: String,
    val link: String,
    val name: String,
    val speakers: List<String>
)