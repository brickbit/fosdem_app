package com.rgr.fosdem.data.model.dao

import com.rgr.fosdem.domain.model.EventBo
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class EventDao(
        @PrimaryKey var id: String,
        var day: String,
        var talk: TalkDao?,
        var speaker: RealmList<SpeakerDao>?,
        var startHour: String,
        var endHour: String,
        var color: String?
): RealmObject {
        constructor() : this(
                id = "",
                day = "",
                talk = TalkDao(),
                speaker = realmListOf(),
                startHour = "",
                endHour = "",
                color = null
        )
}

fun EventDao.toBo(): EventBo = EventBo(
        id = id,
        day = day,
        talk = talk!!.toBo(),
        speaker = speaker!!.map{ it.toBo() },
        startHour = startHour,
        endHour = endHour,
        color = color
)

fun EventBo.toDao(): EventDao = EventDao(
        id = id,
        day = day,
        talk = talk.toDao(),
        speaker = speaker.map{ it.toDao() }.toRealmList(),
        startHour = startHour,
        endHour = endHour,
        color = color
)
