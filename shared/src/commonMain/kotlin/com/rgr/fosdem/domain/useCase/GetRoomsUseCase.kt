package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetRoomsUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: NetworkRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(track: String): Result<List<String>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val filteredRooms = getRooms(schedulesData, track)

        return if(filteredRooms != null) {
            Result.success(filteredRooms)
        } else {
            Result.success(getRooms(jsonProvider.getSchedule().getOrNull(), track)!!)
        }
    }

    private fun getRooms(
        schedulesData: List<TrackBo>?,
        track: String
    ): List<String>? {
        return  schedulesData?.let { schedules ->
            val events = schedules.map { it.events }.flatten()
            val rooms = events
                .filter { if(track != "All" && track.isNotEmpty()) it.talk.track == track else true }
                .map { it.talk.room.name }.distinct()
            rooms.sorted()
        }
    }
}