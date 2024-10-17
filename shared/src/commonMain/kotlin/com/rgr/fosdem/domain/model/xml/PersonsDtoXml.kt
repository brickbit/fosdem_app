package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("persons")
@XmlSerialName(value = "persons")
data class PersonsDtoXml(
    @SerialName("person")
    val person: List<PersonDtoXml>,
)