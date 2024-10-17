package com.rgr.fosdem.data.model.dto

import com.rgr.fosdem.domain.model.StandBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StandDto(
    @SerialName("title")
    val title: String,
    @SerialName("image")
    val image: String,
    @SerialName("features")
    val features: List<StandFeaturesDto>
)

fun StandDto.toBo(): StandBo {
    return StandBo(
        title = title,
        image = image,
        features = features.map{ it.toBo() }
    )
}