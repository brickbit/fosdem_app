package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.repository.InMemoryRepository

class SetFavouriteUseCase(
    private val inMemoryRepository: InMemoryRepository
) {

    operator fun invoke(
        schedule: ScheduleBo,
        list: List<ScheduleBo>,
        favourite: Boolean
    ) {
        val scheduleToModify = list.firstOrNull { it == schedule }
        scheduleToModify?.let {
            val newSchedules = list.toMutableList()
            newSchedules.remove(scheduleToModify)
            newSchedules.add(scheduleToModify.copy(favourite = favourite))
            inMemoryRepository.saveScheduleList(newSchedules.toMutableList().sortedBy { it.date })
        }
    }
}