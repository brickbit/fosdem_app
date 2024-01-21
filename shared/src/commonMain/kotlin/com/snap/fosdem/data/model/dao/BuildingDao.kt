package com.snap.fosdem.data.model.dao

import com.snap.fosdem.domain.model.BuildingBo
import io.realm.kotlin.schema.RealmStorageType
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class BuildingDao(
        @PrimaryKey var id: String,
        var name: String,
        var online: Boolean,
        var map: String
): RealmObject {
        constructor() : this(
                id = "",
                name = "",
                online = false,
                map = ""
        )
}

fun BuildingDao.toBo() = BuildingBo(
        id = id,
        name = name,
        online = online,
        map = map
)

fun BuildingBo.toDao() = BuildingDao(
        id = id,
        name = name,
        online = online,
        map = map
)