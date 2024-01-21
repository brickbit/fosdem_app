package com.snap.fosdem.data.model.dao

import com.snap.fosdem.domain.model.StandBo
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.schema.RealmStorageType
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class StandDao(
    var title: String,
    var image: String,
    var features: RealmList<StandFeaturesDao>?
): RealmObject {
    constructor(): this(
        title = "",
        image = "",
        features = realmListOf()
    )
}

fun StandDao.toBo(): StandBo {
    return StandBo(
        title = title,
        image = image,
        features = features!!.map{ it.toBo() }
    )
}

fun StandBo.toDao(): StandDao {
    return StandDao(
        title = title,
        image = image,
        features = features.map{ it.toDao() }.toRealmList()
    )
}