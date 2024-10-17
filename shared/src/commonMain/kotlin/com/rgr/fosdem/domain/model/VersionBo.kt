package com.rgr.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VersionBo(
    val version: String,
    val date: String
)
