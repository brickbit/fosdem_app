package com.rgr.fosdem.data.dataSource.db.converter

import androidx.room.TypeConverter
import com.rgr.fosdem.domain.model.bo.AttachmentBo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ArrayListAttachmentConverter {


    @TypeConverter
    fun fromAttachmentBoArrayList(value: ArrayList<AttachmentBo>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toAttachmentBoArrayList(value: String): ArrayList<AttachmentBo> {
        return Json.decodeFromString(value)
    }

}