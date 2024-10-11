package com.rgr.fosdem.data.dataSource.db.converter

import androidx.room.TypeConverter
import com.rgr.fosdem.domain.model.StandFeaturesBo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ArrayListStandFeaturesConverter {

    @TypeConverter
    fun fromStandFeaturesBoArrayList(value: ArrayList<StandFeaturesBo>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStandFeaturesBoArrayList(value: String): ArrayList<StandFeaturesBo> {
        return Json.decodeFromString(value)
    }

}