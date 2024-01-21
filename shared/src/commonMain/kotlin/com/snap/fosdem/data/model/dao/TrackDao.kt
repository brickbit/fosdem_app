package com.snap.fosdem.data.model.dao

import com.snap.fosdem.domain.model.TrackBo
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class TrackDao(
        @PrimaryKey var id: String,
        var name: String,
        var events: RealmList<EventDao>?,
        var stands: RealmList<StandDao>?
): RealmObject {
        constructor(): this(
                id = "",
                name = "",
                events = realmListOf(),
                stands = realmListOf()
        )
}

fun TrackDao.toBo() = TrackBo(
        id = id,
        name = name,
        events = events!!.map { it.toBo() },
        stands = stands!!.map { it.toBo() }
)

fun TrackBo.toDao(): TrackDao {
        return TrackDao(
                id = id,
                name = name,
                events = events.map { it.toDao() }.toRealmList(),
                stands = stands.map { it.toDao() }.toRealmList()
        )
}
