package com.rgr.fosdem.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AttachmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val link: String
)