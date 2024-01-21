package com.snap.fosdem.data.model

import com.snap.fosdem.domain.model.StandFeaturesBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StandFeaturesDto(
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("type")
    val type: String,
    @SerialName("companies")
    val companies: MutableList<String>
)

fun StandFeaturesDto.toBo(): StandFeaturesBo {
    return StandFeaturesBo(
        subtitle = subtitle,
        type = type,
        companies = companies
    )
}