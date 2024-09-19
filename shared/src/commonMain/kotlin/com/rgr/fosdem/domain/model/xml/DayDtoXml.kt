package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("day")
@XmlSerialName(value = "day")
data class DayDtoXml(
    @SerialName("index")
    val index: String,
    @SerialName("date")
    val date: String,
    @SerialName("start")
    val start: String,
    @SerialName("end")
    val end: String,
    @SerialName("room")
    val room: List<RoomDtoXml>,
)
