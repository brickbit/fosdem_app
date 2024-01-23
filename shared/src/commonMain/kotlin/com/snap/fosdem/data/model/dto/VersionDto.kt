package com.snap.fosdem.data.model.dto

import com.snap.fosdem.domain.model.VersionBo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionDto(
    @SerialName("version")
    val version: String,
    @SerialName("date")
    val date: String
)

fun VersionDto.toBo() = VersionBo(
    version = version,
    date = date
)
