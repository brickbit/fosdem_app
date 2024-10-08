package com.rgr.fosdem.android.db

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.rgr.fosdem.domain.model.StandFeaturesBo

class ArrayListStandFeaturesConverter {

    val gson = Gson()

    @TypeConverter
    fun fromStandFeaturesBoArrayList(value: ArrayList<StandFeaturesBo>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStandFeaturesBoArrayList(value: String): ArrayList<StandFeaturesBo> {
        val objectType = object : TypeToken<ArrayList<StandFeaturesBo>>() {}.type
        return gson.fromJson(value, objectType)
    }

}