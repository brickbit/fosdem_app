package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VersionBo(
    val version: String,
    val date: String
)
