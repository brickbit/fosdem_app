package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("person")
@XmlSerialName(value = "person")
data class PersonDtoXml(
    @XmlValue val content: String,
    @SerialName("id")
    val id: String,
)