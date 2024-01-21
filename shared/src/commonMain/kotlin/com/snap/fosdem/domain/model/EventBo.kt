package com.snap.fosdem.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class EventBo(
        val id: String,
        val day: String,
        val talk: TalkBo,
        val speaker: List<SpeakerBo>,
        val startHour: String,
        val endHour: String,
        val color: String?,
)

fun EventBo.calculateTimeInMillis(timeBefore: Int): Long? {
        val currentMoment: Instant = Clock.System.now()
        val currentMilliseconds = currentMoment.toEpochMilliseconds()
        val hour = startHour.split(":").getOrNull(0)?.toInt()
        val minutes = startHour.split(":").getOrNull(1)?.toInt()

        if(hour == null || minutes == null) {
                return null
        }

        val eventTime = LocalDateTime(
                year = 2024,
                monthNumber = 2,
                dayOfMonth = if(day == "Saturday") 3 else 4,
                hour = hour,
                minute = minutes
        )
        val eventMilliseconds = eventTime.toInstant(TimeZone.UTC).toEpochMilliseconds()
        val millisecondsBefore = timeBefore * 60 * 1000
        return eventMilliseconds - currentMilliseconds - millisecondsBefore
}
