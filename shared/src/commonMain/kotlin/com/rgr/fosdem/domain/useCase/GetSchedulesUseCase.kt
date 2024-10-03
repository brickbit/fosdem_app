package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.repository.InMemoryRepository
import kotlinx.coroutines.delay

class GetSchedulesUseCase(
    private val inMemoryRepository: InMemoryRepository
) {

    suspend operator fun invoke(
        date: String = "",
        start: List<String> = emptyList(),
        title: String = "",
        track: String = "",
        speaker: String = "",
        room: String = "",
    ): Result<List<ScheduleBo>> {
        delay(3000)
        var data = inMemoryRepository.fetchSchedules()
        data = filterByDate(data = data, date = date)
        data = filterByStart(data = data, start = start)
        data = filterByTrack(data = data, track = track)
        data = filterBySpeaker(data = data, speaker = speaker)
        data = filterByTitle(data = data, title = title)
        data = filterByRoom(data = data, room = room)
        data.ifEmpty { return Result.failure(ErrorType.EmptyScheduleListError) }
        return Result.success(data.toMutableList().sortedBy { it.date })
    }

    private fun filterByDate(
        data: List<ScheduleBo>,
        date: String
    ): List<ScheduleBo> {
        date.ifEmpty { return data }
        return data.filter { it.date.substring(IntRange(0,9)) == date.substring(IntRange(0,9)) }
    }

    private fun filterByStart(
        data: List<ScheduleBo>,
        start: List<String>
    ): List<ScheduleBo> {
        start.ifEmpty { return data }
        val listStart = mutableListOf<ScheduleBo>()
        data.map { schedule ->
            if (schedule.speaker.any { item -> start.contains(item) }) {
                listStart.add(schedule)
            }
        }
        return listStart
    }

    private fun filterByTrack(
        data: List<ScheduleBo>,
        track: String
    ): List<ScheduleBo> {
        track.ifEmpty { return data }
        return data.filter { it.track == track }
    }

    private fun filterByRoom(
        data: List<ScheduleBo>,
        room: String
    ): List<ScheduleBo> {
        room.ifEmpty { return data }
        return data.filter { it.room == room }
    }

    private fun filterBySpeaker(
        data: List<ScheduleBo>,
        speaker: String
    ): List<ScheduleBo> {
        speaker.ifEmpty { return data }
        return data.filter { match(it.speaker, speaker) }

    }

    private fun match(speakerList: List<String>, speaker: String): Boolean {
        return speaker.all { person -> speakerList.any { item -> item.contains(person) } }
    }

    private fun filterByTitle(
        data: List<ScheduleBo>,
        title: String
    ): List<ScheduleBo> {
        title.ifEmpty { return data }
        return data.filter { it.title.contains(title) }
    }

}