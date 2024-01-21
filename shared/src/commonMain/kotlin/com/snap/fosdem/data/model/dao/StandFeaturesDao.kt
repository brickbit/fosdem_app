package com.snap.fosdem.data.model.dao

import com.snap.fosdem.domain.model.StandFeaturesBo
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.schema.RealmStorageType
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class StandFeaturesDao(
    var subtitle: String,
    var type: String,
    var companies: RealmList<String>
): RealmObject {
    constructor(): this(
        subtitle = "",
        type = "",
        companies = realmListOf()
    )
}

fun StandFeaturesDao.toBo(): StandFeaturesBo {
    return StandFeaturesBo(
        subtitle = subtitle,
        type = type,
        companies = companies
    )
}

fun StandFeaturesBo.toDao(): StandFeaturesDao {
    return StandFeaturesDao(
        subtitle = subtitle,
        type = type,
        companies = companies.toRealmList()
    )
}