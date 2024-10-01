package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.repository.InMemoryRepository

class GetSchedulesUseCase(
    private val inMemoryRepository: InMemoryRepository
) {

    operator fun invoke(
        date: String = "",
        start: String = "",
        duration: String = "",
        title: String = "",
        track: String = "",
        type: String = "",
        speaker: String = "",
    ): Result<List<ScheduleBo>> {
        var data = inMemoryRepository.fetchSchedules()
        data = filterByDate(data = data, date = date)
        data = filterByStart(data = data, start = start)
        data = filterByDuration(data = data, duration = duration)
        data = filterByTrack(data = data, track = track)
        data = filterByType(data = data, type = type)
        data = filterBySpeaker(data = data, speaker = speaker)
        data = filterByTitle(data = data, title = title)
        data.ifEmpty { return Result.failure(ErrorType.EmptyScheduleListError) }
        return Result.success(data)
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
        start: String
    ): List<ScheduleBo> {
        start.ifEmpty { return data }
        return data.filter { it.date.substring(IntRange(0,9)) == start.substring(IntRange(0,9)) }
    }

    private fun filterByDuration(
        data: List<ScheduleBo>,
        duration: String
    ): List<ScheduleBo> {
        duration.ifEmpty { return data }
        return data.filter { it.date.substring(IntRange(0,9)) == duration.substring(IntRange(0,9)) }
    }

    private fun filterByTrack(
        data: List<ScheduleBo>,
        track: String
    ): List<ScheduleBo> {
        track.ifEmpty { return data }
        return data.filter { it.date.substring(IntRange(0,9)) == track.substring(IntRange(0,9)) }
    }

    private fun filterByType(
        data: List<ScheduleBo>,
        type: String
    ): List<ScheduleBo> {
        type.ifEmpty { return data }
        return data.filter { it.date.substring(IntRange(0,9)) == type.substring(IntRange(0,9)) }
    }

    private fun filterBySpeaker(
        data: List<ScheduleBo>,
        speaker: String
    ): List<ScheduleBo> {
        speaker.ifEmpty { return data }
        return data.filter { it.speaker.contains(speaker) }
    }

    private fun filterByTitle(
        data: List<ScheduleBo>,
        title: String
    ): List<ScheduleBo> {
        title.ifEmpty { return data }
        return data.filter { it.title.contains(title) }
    }

}