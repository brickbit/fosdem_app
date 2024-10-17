package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("attachment")
@XmlSerialName(value = "attachment")
data class AttachmentDtoXml(
    @XmlValue val content: String,
    @SerialName("type")
    val type: String,
    @SerialName("href")
    val href: String,
)