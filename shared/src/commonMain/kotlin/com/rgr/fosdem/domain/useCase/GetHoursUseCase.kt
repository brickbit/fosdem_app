package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.NetworkRepository

class GetHoursUseCase(
    private val repository: NetworkRepository,
) {
    suspend operator fun invoke(day: String): Result<List<String>> {
        return Result.failure(Error())
        //return Result.success(getHour(jsonProvider.getSchedule().getOrNull(), day)!!)
    }

    private fun getHour(
        schedulesData: List<TrackBo>?,
        day: String
    ): List<String>? {
        return schedulesData?.let { schedules ->
            val events = schedules.map { it.events }.flatten()
            val hours = events.filter { it.day == day }.map { it.startHour }.filter { it[0].isDigit() }.distinct()
            hours.sorted()
        }
    }
}