package com.snap.fosdem.app.di

import com.snap.fosdem.data.local.LocalRepositoryImpl
import com.snap.fosdem.data.repository.ScheduleRepositoryImpl
import com.snap.fosdem.domain.repository.LocalRepository
import com.snap.fosdem.domain.repository.ScheduleRepository
import com.snap.fosdem.domain.useCase.ChangeLanguageUseCase
import com.snap.fosdem.domain.useCase.GetEventByIdUseCase
import com.snap.fosdem.domain.useCase.GetLanguageUseCase
import com.snap.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByBuildingUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByTrackUseCase
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import com.snap.fosdem.domain.useCase.GetTracksUseCase
import com.snap.fosdem.domain.useCase.SaveOnBoardingUseCase
import com.snap.fosdem.domain.useCase.SavePreferredTracksUseCase
import org.koin.dsl.module

val repositoryModule = module {
    factory<ScheduleRepository> { ScheduleRepositoryImpl() }
    factory<LocalRepository> { LocalRepositoryImpl(get()) }
}
val useCaseModule = module {
    single { GetScheduleDataUseCase(get()) }
    single { GetTracksUseCase(get()) }
    single { SaveOnBoardingUseCase(get()) }
    single { GetOnBoardingStatusUseCase(get()) }
    single { SavePreferredTracksUseCase(get()) }
    single { GetPreferredTracksUseCase(get()) }
    single { GetScheduleByTrackUseCase(get()) }
    single { GetScheduleByHourUseCase(get()) }
    single { GetScheduleByBuildingUseCase(get()) }
    single { GetEventByIdUseCase(get()) }
    single { GetLanguageUseCase(get()) }
    single { ChangeLanguageUseCase(get()) }
}