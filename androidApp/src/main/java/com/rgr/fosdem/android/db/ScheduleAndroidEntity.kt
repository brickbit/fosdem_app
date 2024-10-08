package com.rgr.fosdem.android.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.rgr.fosdem.domain.model.bo.AttachmentBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo

@Entity(tableName = "schedule")
class ScheduleAndroidEntity (
    @PrimaryKey var id: String = "",
    var date: String = "",
    var start: String = "",
    var duration: String = "",
    var title: String = "",
    var subtitle: String = "",
    var track: String = "",
    var type: String = "",
    var language: String = "",
    var abstract: String = "",
    var description: String = "",
    var feedbackUrl: String = "",
    var room: String = "",
    var speaker: ArrayList<String> = arrayListOf(),
    var attachment: ArrayList<AttachmentBo> = arrayListOf(),
    var favourite: Boolean = false,
    var year: String = "",
)

fun ScheduleBo.toEntity(): ScheduleAndroidEntity {
    return ScheduleAndroidEntity(
        id = id,
        date = date,
        start = start,
        duration = duration,
        title = title,
        subtitle = subtitle,
        track = track,
        type = type,
        language = language,
        abstract = abstract,
        description = description,
        feedbackUrl = feedbackUrl,
        room = room,
        favourite = favourite,
        speaker = ArrayList(speaker),
        attachment = ArrayList(attachment),
        year = year
    )
}

fun ScheduleAndroidEntity.toBo(): ScheduleBo {
    return ScheduleBo(
        id = id,
        date = date,
        start = start,
        duration = duration,
        title = title,
        subtitle = subtitle,
        track = track,
        type = type,
        language = language,
        abstract = abstract,
        description = description,
        feedbackUrl = feedbackUrl,
        room = room,
        favourite = favourite,
        year = year,
        speaker = speaker.toList(),
        attachment = attachment.toList()
    )
}



