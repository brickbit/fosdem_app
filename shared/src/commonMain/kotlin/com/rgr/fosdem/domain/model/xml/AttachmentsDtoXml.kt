package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("attachments")
@XmlSerialName(value = "attachments")
data class AttachmentsDtoXml(
    @SerialName("attachment")
    val attachment: List<AttachmentDtoXml>,
)