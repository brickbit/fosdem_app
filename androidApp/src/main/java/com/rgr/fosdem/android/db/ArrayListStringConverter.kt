package com.rgr.fosdem.android.db

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ArrayListStringConverter {

    val gson = Gson()

    @TypeConverter
    fun fromStringArrayList(value: ArrayList<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): ArrayList<String> {
        val objectType = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(value, objectType)
    }

}