package com.snap.fosdem.data.model.dto

import com.snap.fosdem.domain.model.SpeakerBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpeakerDto(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("image")
        val image: String?,
        @SerialName("description")
        val description: String?,
)

fun SpeakerDto.toBo() = SpeakerBo(
        id = id,
        name = name,
        image = image,
        description = description
)