package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetRoomsUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val filteredRooms = schedulesData?.let { schedules ->
            val events = schedules.map { it.events }.flatten()
            val rooms = events.map { it.talk.room.name }.distinct()
            rooms.sorted()
        }
        return filteredRooms?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}