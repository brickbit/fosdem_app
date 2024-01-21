package com.snap.fosdem.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StandBo(
    val title: String,
    val image: String,
    val features: List<StandFeaturesBo>
)


