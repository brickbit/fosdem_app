package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("links")
@XmlSerialName(value = "links")
data class LinksDtoXml(
    @SerialName("link")
    val link: List<LinkDtoXml>,
)