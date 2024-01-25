package com.rgr.fosdem.data.model.dto

import com.rgr.fosdem.domain.model.VersionBo
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
