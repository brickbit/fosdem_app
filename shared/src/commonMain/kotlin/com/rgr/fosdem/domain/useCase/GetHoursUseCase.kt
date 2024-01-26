package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.JsonProvider
import com.rgr.fosdem.domain.repository.RealmRepository
import com.rgr.fosdem.domain.repository.ScheduleRepository

class GetHoursUseCase(
    private val jsonProvider: JsonProvider,
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(day: String): Result<List<String>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val filteredHours = getHour(schedulesData, day)

        return if(filteredHours != null) {
            Result.success(getHour(schedulesData, day)!!)
        } else {
            Result.success(getHour(jsonProvider.getSchedule().getOrNull(), day)!!)
        }
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