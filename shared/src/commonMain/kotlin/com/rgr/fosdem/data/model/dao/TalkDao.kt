package com.rgr.fosdem.data.model.dao

import com.rgr.fosdem.domain.model.TalkBo
import io.realm.kotlin.schema.RealmStorageType
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class TalkDao(
        @PrimaryKey var id: String,
        var title: String,
        var description: String,
        var track: String,
        var room: RoomDao?,
        var day: String,
        var start: String,
        var end: String
): RealmObject {
        constructor(): this(
                id = "",
                title = "",
                description = "",
                track = "",
                room = RoomDao(),
                day = "",
                start = "",
                end = ""
        )
}

fun TalkDao.toBo() = TalkBo(
        id = id,
        title = title,
        description = description,
        track = track,
        room = room!!.toBo(),
        day = day,
        start = start,
        end = end
)

fun TalkBo.toDao() = TalkDao(
        id = id,
        title = title,
        description = description,
        track = track,
        room = room.toDao(),
        day = day,
        start = start,
        end = end
)
