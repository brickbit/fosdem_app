package com.rgr.fosdem.android.db

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.rgr.fosdem.domain.model.bo.AttachmentBo

class ArrayListAttachmentConverter {

    val gson = Gson()

    @TypeConverter
    fun fromAttachmentBoArrayList(value: ArrayList<AttachmentBo>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAttachmentBoArrayList(value: String): ArrayList<AttachmentBo> {
        val objectType = object : TypeToken<ArrayList<AttachmentBo>>() {}.type
        return gson.fromJson(value, objectType)
    }

}