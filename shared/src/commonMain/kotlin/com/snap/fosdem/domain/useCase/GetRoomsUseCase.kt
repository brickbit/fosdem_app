package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.ScheduleRepository

class GetRoomsUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        val filteredRooms = repository.getSchedule().getOrNull()?.let { schedules ->
            val events = schedules.map { it.events }.flatten()
            val rooms = events.mapNotNull { it.talk?.room?.name }.distinct()
            rooms.sorted()
        }
        return filteredRooms?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}