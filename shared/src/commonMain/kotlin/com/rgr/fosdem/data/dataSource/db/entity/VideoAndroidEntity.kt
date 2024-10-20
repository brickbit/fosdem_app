package com.rgr.fosdem.data.dataSource.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rgr.fosdem.domain.model.bo.VideoBo

@Entity(tableName = "videos")
data class VideoAndroidEntity(
    @PrimaryKey val idTalk: String,
    val link: String,
    val name: String,
    val type: String,
    val year: String,
    val speakers: ArrayList<String>
)

fun VideoBo.toEntity(): VideoAndroidEntity {
    return VideoAndroidEntity(
        idTalk = idTalk,
        link = link,
        name = name,
        type = type,
        year = year,
        speakers = ArrayList(speakers)
    )
}

fun VideoAndroidEntity.toBo(): VideoBo {
    return VideoBo(
        idTalk = idTalk,
        link = link,
        name = name,
        type = type,
        year = year,
        speakers = speakers.toList()
    )
}