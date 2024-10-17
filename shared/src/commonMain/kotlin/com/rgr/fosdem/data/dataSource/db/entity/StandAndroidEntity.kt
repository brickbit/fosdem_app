package com.rgr.fosdem.data.dataSource.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.StandFeaturesBo

@Entity(tableName = "stands")
data class StandAndroidEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val image: String,
    val features: ArrayList<StandFeaturesBo>
)

fun StandBo.toEntity(): StandAndroidEntity {
    return StandAndroidEntity(
        title = title,
        image = image,
        features = ArrayList(features)
    )
}

fun StandAndroidEntity.toBo(): StandBo {
    return StandBo(
        title = title,
        image = image,
        features = features.toList()
    )
}