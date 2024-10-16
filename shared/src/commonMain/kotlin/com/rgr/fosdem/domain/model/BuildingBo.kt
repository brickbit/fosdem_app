package com.rgr.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BuildingBo(
        val id: String,
        val name: String,
        val online: Boolean,
        val map: String,
)