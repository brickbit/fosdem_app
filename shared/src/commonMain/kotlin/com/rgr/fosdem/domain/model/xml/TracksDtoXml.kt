package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("tracks")
@XmlSerialName(value = "tracks")
data class TracksDtoXml(
    @SerialName("track")
    val track: List<TrackDtoXml>,
)
