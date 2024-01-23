package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository

class GetHoursUseCase(
    private val repository: ScheduleRepository,
    private var realmRepository: RealmRepository
) {
    suspend operator fun invoke(day: String): Result<List<String>> {
        val realmResult = realmRepository.getSchedule()
        val schedulesData = realmResult.ifEmpty {
            repository.getSchedule().getOrNull()
        }
        val filteredHours = schedulesData?.let { schedules ->
            val events = schedules.map { it.events }.flatten()
            val hours = events.filter { it.day == day }.map { it.startHour }.filter { it[0].isDigit() }.distinct()
            hours.sorted()
        }

        return filteredHours?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}