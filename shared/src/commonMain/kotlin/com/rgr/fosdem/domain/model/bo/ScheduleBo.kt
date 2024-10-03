package com.rgr.fosdem.domain.model.bo

data class ScheduleBo(
    val id: String,
    val date: String,
    val start: String,
    val duration: String,
    val title: String,
    val subtitle: String,
    val track: String,
    val type: String,
    val language: String,
    val abstract: String,
    val description: String,
    val feedbackUrl: String,
    val attachment: List<AttachmentBo>,
    val speaker: List<String>,
    val room: String,
    val favourite: Boolean = false,
    val year: String,
)