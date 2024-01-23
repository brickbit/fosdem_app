package com.snap.fosdem.app.di

import com.snap.fosdem.data.local.LocalRepositoryImpl
import com.snap.fosdem.data.repository.RealmRepositoryImpl
import com.snap.fosdem.data.repository.ScheduleRepositoryImpl
import com.snap.fosdem.domain.repository.LocalRepository
import com.snap.fosdem.domain.repository.RealmRepository
import com.snap.fosdem.domain.repository.ScheduleRepository
import com.snap.fosdem.domain.useCase.ChangeLanguageUseCase
import com.snap.fosdem.domain.useCase.GetEventByIdUseCase
import com.snap.fosdem.domain.useCase.GetEventsForNotificationUseCase
import com.snap.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.snap.fosdem.domain.useCase.GetHoursUseCase
import com.snap.fosdem.domain.useCase.GetLanguageUseCase
import com.snap.fosdem.domain.useCase.GetNotificationTimeUseCase
import com.snap.fosdem.domain.useCase.GetNotificationsEnabledUseCase
import com.snap.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.snap.fosdem.domain.useCase.GetRoomsUseCase
import com.snap.fosdem.domain.useCase.GetSavedTracksUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByParameterUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByTrackUseCase
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import com.snap.fosdem.domain.useCase.GetSpeakersUseCase
import com.snap.fosdem.domain.useCase.GetStandsUseCase
import com.snap.fosdem.domain.useCase.GetTracksUseCase
import com.snap.fosdem.domain.useCase.IsEventNotifiedUseCase
import com.snap.fosdem.domain.useCase.ManageEventNotificationUseCase
import com.snap.fosdem.domain.useCase.ManageNotificationPermissionUseCase
import com.snap.fosdem.domain.useCase.ManageNotificationTimeUseCase
import com.snap.fosdem.domain.useCase.SaveOnBoardingUseCase
import com.snap.fosdem.domain.useCase.SavePreferredTracksUseCase
import org.koin.dsl.module

val repositoryModule = module {
    factory<ScheduleRepository> { ScheduleRepositoryImpl() }
    factory<LocalRepository> { LocalRepositoryImpl(get()) }
    factory<RealmRepository> { RealmRepositoryImpl() }
}
val useCaseModule = module {
    single { GetScheduleDataUseCase(get(),get()) }
    single { GetTracksUseCase(get(), get()) }
    single { SaveOnBoardingUseCase(get()) }
    single { GetOnBoardingStatusUseCase(get()) }
    single { SavePreferredTracksUseCase(get()) }
    single { GetPreferredTracksUseCase(get()) }
    single { GetScheduleByTrackUseCase(get(), get()) }
    single { GetScheduleByHourUseCase(get(), get()) }
    single { GetScheduleByParameterUseCase(get(), get()) }
    single { GetHoursUseCase(get(), get()) }
    single { GetRoomsUseCase(get(), get()) }
    single { GetSavedTracksUseCase(get()) }
    single { GetFavouritesEventsUseCase(get()) }
    single { GetEventByIdUseCase(get(), get()) }
    single { GetLanguageUseCase(get()) }
    single { ChangeLanguageUseCase(get()) }
    single { ManageNotificationPermissionUseCase(get()) }
    single { GetNotificationsEnabledUseCase(get()) }
    single { ManageEventNotificationUseCase(get()) }
    single { IsEventNotifiedUseCase(get()) }
    single { GetEventsForNotificationUseCase(get()) }
    single { GetNotificationTimeUseCase(get()) }
    single { ManageNotificationTimeUseCase(get()) }
    single { GetSpeakersUseCase(get(), get()) }
    single { GetStandsUseCase(get(), get()) }
}