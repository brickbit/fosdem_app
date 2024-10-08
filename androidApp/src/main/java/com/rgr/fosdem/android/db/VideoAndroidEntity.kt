package com.rgr.fosdem.android.db

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
)

fun VideoBo.toEntity(): VideoAndroidEntity {
    return VideoAndroidEntity(
        idTalk = idTalk,
        link = link,
        name = name,
        type = type,
        year = year
    )
}

fun VideoAndroidEntity.toBo(): VideoBo {
    return VideoBo(
        idTalk = idTalk,
        link = link,
        name = name,
        type = type,
        year = year,
        speakers = emptyList()
    )
}