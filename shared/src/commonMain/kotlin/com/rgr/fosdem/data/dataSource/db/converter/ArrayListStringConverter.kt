package com.rgr.fosdem.data.dataSource.db.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ArrayListStringConverter {

    @TypeConverter
    fun fromStringArrayList(value: ArrayList<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): ArrayList<String> {
        return Json.decodeFromString(value)
    }

}