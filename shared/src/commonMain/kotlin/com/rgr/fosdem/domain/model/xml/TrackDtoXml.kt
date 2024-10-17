package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("track")
@XmlSerialName(value = "track")
data class TrackDtoXml(
    @SerialName("online_qa")
    val online_qa: String,
    @XmlValue val content: String,
)