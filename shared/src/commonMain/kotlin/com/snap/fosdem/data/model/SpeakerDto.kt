package com.snap.fosdem.data.model

import com.snap.fosdem.domain.model.SpeakerBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpeakerDto(
        @SerialName("name")
        val name: String,
        @SerialName("image")
        val image: String?,
        @SerialName("description")
        val description: String?,
)

fun SpeakerDto.toBo() = SpeakerBo(
        name = name,
        image = image,
        description = description
)
