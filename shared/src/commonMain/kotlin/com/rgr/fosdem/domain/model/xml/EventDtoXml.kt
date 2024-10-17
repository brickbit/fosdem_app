package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("event")
@XmlSerialName(value = "event")
data class EventDtoXml(
    @SerialName("guid")
    val guid: String,
    @SerialName("id")
    val id: String,
    @SerialName("date")
    @XmlChildrenName("date")
    val date: String,
    @SerialName("start")
    @XmlChildrenName("start")
    val start: String,
    @SerialName("duration")
    @XmlChildrenName("duration")
    val duration: String,
    @SerialName("room")
    @XmlChildrenName("room")
    val room: String,
    @SerialName("slug")
    @XmlChildrenName("slug")
    val slug: String,
    @SerialName("url")
    @XmlChildrenName("url")
    val url: String,
    @SerialName("title")
    @XmlChildrenName("title")
    val title: String,
    @SerialName("subtitle")
    @XmlChildrenName("subtitle")
    val subtitle: String?,
    @SerialName("track")
    @XmlChildrenName("track")
    val track: String,
    @SerialName("type")
    @XmlChildrenName("type")
    val type: String,
    @SerialName("language")
    @XmlChildrenName("language")
    val language: String,
    @SerialName("abstract")
    @XmlChildrenName("abstract")
    val abstract: String,
    @SerialName("description")
    @XmlChildrenName("description")
    val description: String?,
    @SerialName("feedback_url")
    @XmlChildrenName("feedback_url")
    val feedbackUrl: String,
    @SerialName("persons")
    @XmlChildrenName("persons")
    val persons: PersonsDtoXml,
    @SerialName("attachments")
    @XmlChildrenName("attachments")
    val attachments: AttachmentsDtoXml,
    @SerialName("links")
    @XmlChildrenName("links")
    val links: LinksDtoXml
)