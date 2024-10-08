package com.rgr.fosdem.app.di

import com.rgr.fosdem.data.local.LocalRepositoryImpl
import com.rgr.fosdem.data.repository.InMemoryRepositoryImpl
import com.rgr.fosdem.data.repository.NetworkRepositoryImpl
import com.rgr.fosdem.domain.repository.InMemoryRepository
import com.rgr.fosdem.domain.repository.LocalRepository
import com.rgr.fosdem.domain.repository.NetworkRepository
import com.rgr.fosdem.domain.useCase.ChangeLanguageUseCase
import com.rgr.fosdem.domain.useCase.GetDaysUseCase
import com.rgr.fosdem.domain.useCase.GetEventByIdUseCase
import com.rgr.fosdem.domain.useCase.GetEventsForNotificationUseCase
import com.rgr.fosdem.domain.useCase.GetFavouriteSchedulesUseCase
import com.rgr.fosdem.domain.useCase.GetFavouritesEventsUseCase
import com.rgr.fosdem.domain.useCase.GetHoursUseCase
import com.rgr.fosdem.domain.useCase.GetLanguageUseCase
import com.rgr.fosdem.domain.useCase.GetNewHoursUseCase
import com.rgr.fosdem.domain.useCase.GetNewRoomsUseCase
import com.rgr.fosdem.domain.useCase.GetNewTracksUseCase
import com.rgr.fosdem.domain.useCase.GetNotificationTimeUseCase
import com.rgr.fosdem.domain.useCase.GetNotificationsEnabledUseCase
import com.rgr.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksShownUseCase
import com.rgr.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.rgr.fosdem.domain.useCase.GetRightNowSchedulesUseCase
import com.rgr.fosdem.domain.useCase.GetRoomsUseCase
import com.rgr.fosdem.domain.useCase.GetSavedTracksUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByParameterUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleByTrackUseCase
import com.rgr.fosdem.domain.useCase.GetScheduleDataUseCase
import com.rgr.fosdem.domain.useCase.GetSchedulesUseCase
import com.rgr.fosdem.domain.useCase.GetSpeakersUseCase
import com.rgr.fosdem.domain.useCase.GetStandsUseCase
import com.rgr.fosdem.domain.useCase.GetTracksUseCase
import com.rgr.fosdem.domain.useCase.GetVideoUseCase
import com.rgr.fosdem.domain.useCase.IsUpdateNeeded
import com.rgr.fosdem.domain.useCase.IsEventNotifiedUseCase
import com.rgr.fosdem.domain.useCase.LoadSchedulesUseCase
import com.rgr.fosdem.domain.useCase.LoadSpeakersUseCase
import com.rgr.fosdem.domain.useCase.LoadStandsUseCase
import com.rgr.fosdem.domain.useCase.LoadVideosUseCase
import com.rgr.fosdem.domain.useCase.ManageEventNotificationUseCase
import com.rgr.fosdem.domain.useCase.ManageNotificationPermissionUseCase
import com.rgr.fosdem.domain.useCase.ManageNotificationTimeUseCase
import com.rgr.fosdem.domain.useCase.SaveFavouriteTracksShownUseCase
import com.rgr.fosdem.domain.useCase.SaveOnBoardingUseCase
import com.rgr.fosdem.domain.useCase.SavePreferredTracksUseCase
import com.rgr.fosdem.domain.useCase.SetFavouriteUseCase
import org.koin.dsl.module

val repositoryModule = module {
    single<InMemoryRepository> { InMemoryRepositoryImpl() }
    factory<NetworkRepository> { NetworkRepositoryImpl() }
    factory<LocalRepository> { LocalRepositoryImpl(get()) }
}
val useCaseModule = module {
    single { GetScheduleDataUseCase(get(),get()) }
    single { GetTracksUseCase(get(), get()) }
    single { SaveOnBoardingUseCase(get()) }
    single { SaveFavouriteTracksShownUseCase(get()) }
    single { GetPreferredTracksShownUseCase(get()) }
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
    single { IsUpdateNeeded(get(), get()) }

    single { LoadSchedulesUseCase(get(), get()) }
    single { LoadVideosUseCase(get(), get()) }
    single { GetSchedulesUseCase(get()) }
    single { GetVideoUseCase(get()) }
    single { GetNewHoursUseCase(get()) }
    single { GetDaysUseCase(get()) }
    single { GetNewTracksUseCase(get()) }
    single { GetNewRoomsUseCase(get()) }
    single { GetFavouriteSchedulesUseCase(get()) }
    single { GetRightNowSchedulesUseCase(get()) }
    single { SetFavouriteUseCase(get()) }
    single { LoadStandsUseCase(get(), get()) }
    single { LoadSpeakersUseCase(get(), get()) }
}