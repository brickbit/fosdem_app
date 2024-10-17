package com.rgr.fosdem.domain.model.xml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("conference")
@XmlSerialName(value = "conference")
data class ConferenceDtoXml(
    @SerialName("acronym")
    @XmlChildrenName("acronym")
    val acronym: String,
    @SerialName("title")
    @XmlChildrenName("title")
    val title: String,
    @SerialName("subtitle")
    @XmlChildrenName("subtitle")
    val subtitle: String?,
    @SerialName("venue")
    @XmlChildrenName("venue")
    val venue: String,
    @SerialName("city")
    @XmlChildrenName("city")
    val city: String,
    @SerialName("start")
    @XmlChildrenName("start")
    val start: String,
    @SerialName("end")
    @XmlChildrenName("end")
    val end: String,
    @SerialName("days")
    @XmlChildrenName("days")
    val days: String,
    @SerialName("day_change")
    @XmlChildrenName("day_change")
    val dayChange: String,
    @SerialName("timeslot_duration")
    @XmlChildrenName("timeslot_duration")
    val timeslotDuration: String,
    @SerialName("base_url")
    @XmlChildrenName("base_url")
    val baseUrl: String,
    @SerialName("time_zone_name")
    @XmlChildrenName("time_zone_name")
    val timeZoneName: String,
)