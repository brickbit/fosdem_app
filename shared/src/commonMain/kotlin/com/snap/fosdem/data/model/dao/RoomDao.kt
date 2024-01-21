package com.snap.fosdem.data.model.dao

import com.snap.fosdem.domain.model.RoomBo
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RoomDao(
    @PrimaryKey var id: String,
    var name: String,
    var capacity: String,
    var building: BuildingDao?,
    var location: String,
    var video: String,
    var chat: String,
): RealmObject {
    constructor(): this(
        id = "",
        name = "",
        capacity = "",
        building = BuildingDao(),
        location = "",
        video = "",
        chat = ""
    )
}

fun RoomDao.toBo() = RoomBo(
        id = id,
        name = name,
        capacity = capacity,
        building = building!!.toBo(),
        location = location,
        video = video,
        chat = chat
)

fun RoomBo.toDao() = RoomDao(
    id = id,
    name = name,
    capacity = capacity,
    building = building.toDao(),
    location = location,
    video = video,
    chat = chat
)

