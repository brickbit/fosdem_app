package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("room")
@XmlSerialName(value = "room")
data class RoomDtoXml(
    @SerialName("name")
    val name: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("event")
    val event: List<EventDtoXml>,
)