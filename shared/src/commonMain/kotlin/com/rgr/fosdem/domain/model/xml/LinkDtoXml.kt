package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("link")
@XmlSerialName(value = "link")
data class LinkDtoXml(
    @XmlValue val content: String,
    @SerialName("href")
    val href: String,
)