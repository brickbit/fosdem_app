package com.rgr.fosdem.data.model.dto

import com.rgr.fosdem.domain.model.StandFeaturesBo
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