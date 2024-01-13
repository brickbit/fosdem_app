package com.snap.fosdem.app.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.snap.fosdem.app.viewModel.MainActivityViewModel
import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.app.viewModel.OnBoardingViewModel
import com.snap.fosdem.app.viewModel.PreferencesViewModel
import com.snap.fosdem.app.viewModel.SpeakerViewModel
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.viewModel.TalkViewModel
import com.snap.fosdem.data.local.LocalRepositoryImpl
import com.snap.fosdem.data.local.dataStorePreferences
import com.snap.fosdem.data.repository.ScheduleRepositoryImpl
import com.snap.fosdem.domain.repository.LocalRepository
import com.snap.fosdem.domain.repository.ScheduleRepository
import com.snap.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import com.snap.fosdem.domain.useCase.GetTracksUseCase
import com.snap.fosdem.domain.useCase.SaveOnBoardingUseCase
import com.snap.fosdem.domain.useCase.SavePreferredTracksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.dsl.module
import org.koin.core.component.get

val providerModule = module {
    single<DataStore<Preferences>> {
        dataStorePreferences(
            corruptionHandler = null,
            coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
            migrations = emptyList(),
            calculatedPath = ""
        )
    }
}
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
}
val viewModelModules = module {
    single { SplashViewModel(get(), get()) }
    single { OnBoardingViewModel(get()) }
    single { PreferencesViewModel(get(), get()) }
    single { MainViewModel(get()) }
    single { SpeakerViewModel() }
    single { TalkViewModel() }
}

object GetViewModels: KoinComponent {
    fun getSplashViewModel() = get<SplashViewModel>()
    fun getOnBoardingViewModel() = get<OnBoardingViewModel>()
    fun getPreferencesViewModel() = get<PreferencesViewModel>()
    fun getMainViewModel() = get<MainViewModel>()
    fun getSpeakerViewModel() = get<SpeakerViewModel>()
    fun getTalkViewModel() = get<TalkViewModel>()
}