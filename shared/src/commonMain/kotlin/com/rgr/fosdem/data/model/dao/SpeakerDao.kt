package com.rgr.fosdem.data.model.dao

import com.rgr.fosdem.domain.model.SpeakerBo
import io.realm.kotlin.schema.RealmStorageType
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SpeakerDao(
        @PrimaryKey var id: String,
        var name: String,
        var image: String?,
        var description: String?
): RealmObject {
        constructor(): this(
                id = "",
                name = "",
                image = "",
                description = ""
        )
}

fun SpeakerDao.toBo() = SpeakerBo(
        id = id,
        name = name,
        image = image,
        description = description
)

fun SpeakerBo.toDao() = SpeakerDao(
        id = id,
        name = name,
        image = image,
        description = description
)
