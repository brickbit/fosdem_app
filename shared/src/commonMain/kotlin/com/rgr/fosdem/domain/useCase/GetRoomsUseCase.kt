package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository

class GetRoomsUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(track: String): Result<List<String>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val filteredRooms = schedulesData?.let { schedules ->
            val events = schedules.map { it.events }.flatten()
            val rooms = events
                .filter { if(track != "All" && track.isNotEmpty()) it.talk.track == track else true }
                .map { it.talk.room.name }.distinct()
            rooms.sorted()
        }
        return filteredRooms?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}