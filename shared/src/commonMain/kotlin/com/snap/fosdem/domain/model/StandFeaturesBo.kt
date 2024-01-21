package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StandFeaturesBo(
    val subtitle: String,
    val type: String,
    val companies: MutableList<String>
)