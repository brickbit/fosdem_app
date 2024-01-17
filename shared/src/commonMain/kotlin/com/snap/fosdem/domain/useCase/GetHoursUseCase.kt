package com.snap.fosdem.domain.useCase

import com.snap.fosdem.domain.repository.ScheduleRepository

class GetHoursUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        val filteredHours = repository.getSchedule().getOrNull()?.let { schedules ->
            val events = schedules.map { it.events }.flatten()
            val hours = events.map { it.startHour }.filter { it[0].isDigit() }.distinct()
            hours.sorted()
        }
        return filteredHours?.let {
            Result.success(it)
        } ?: Result.failure(Error())
    }
}