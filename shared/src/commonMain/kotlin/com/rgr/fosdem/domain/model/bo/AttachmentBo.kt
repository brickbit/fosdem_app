package com.rgr.fosdem.domain.model.bo

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentBo(
    val name: String,
    val link: String
)