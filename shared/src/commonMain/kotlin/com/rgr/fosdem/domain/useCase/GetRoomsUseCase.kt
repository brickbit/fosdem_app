package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetRoomsUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(track: String): Result<List<String>> {
        return Result.success(getRooms(jsonProvider.getSchedule().getOrNull(), track)!!)
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