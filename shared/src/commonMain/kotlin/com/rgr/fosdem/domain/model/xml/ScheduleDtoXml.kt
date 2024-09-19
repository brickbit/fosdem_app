package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("schedule")
@XmlSerialName(value = "schedule")
data class ScheduleDtoXml(
    @SerialName("version")
    @XmlChildrenName("version")
    val version: String,
    @SerialName("conference")
    @XmlChildrenName("conference")
    val conference: ConferenceDtoXml,
    @SerialName("tracks")
    @XmlChildrenName("tracks")
    val tracks: TracksDtoXml,
    @SerialName("day")
    val day: List<DayDtoXml>,
)