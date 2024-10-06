package com.rgr.fosdem.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rgr.fosdem.domain.model.bo.AttachmentBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo

@Entity
data class ScheduleEntity(
    @PrimaryKey
    val id: String,
    val date: String,
    val start: String,
    val duration: String,
    val title: String,
    val subtitle: String,
    val track: String,
    val type: String,
    val language: String,
    val abstract: String,
    val description: String,
    val feedbackUrl: String,
    //val attachment: List<AttachmentBo>,
    //val speaker: List<String>,
)

fun ScheduleBo.toEntity(): ScheduleEntity {
    return ScheduleEntity(
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
        //attachment = attachment,
        //speaker = speaker
    )
}
