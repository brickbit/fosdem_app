package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BuildingBo(
        val name: String,
        val online: Boolean,
        val map: String,
)