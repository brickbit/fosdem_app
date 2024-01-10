package com.snap.fosdem.android.di

import com.snap.fosdem.app.viewModel.MainActivityViewModel
import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.app.viewModel.OnBoardingViewModel
import com.snap.fosdem.app.viewModel.PreferencesViewModel
import com.snap.fosdem.app.viewModel.SpeakerViewModel
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.viewModel.TalkViewModel
import com.snap.fosdem.data.repository.ScheduleRepositoryImpl
import com.snap.fosdem.domain.repository.ScheduleRepository
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import com.snap.fosdem.domain.useCase.GetTracksUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    factory<ScheduleRepository> { ScheduleRepositoryImpl() }
}
val useCaseModule = module {
    single { GetScheduleDataUseCase(get()) }
    single { GetTracksUseCase(get()) }
}
val viewModelModules = module {
    viewModel { MainActivityViewModel() }
    viewModel { SplashViewModel(get()) }
    viewModel { OnBoardingViewModel() }
    viewModel { PreferencesViewModel(get()) }
    viewModel { MainViewModel() }
    viewModel { SpeakerViewModel() }
    viewModel { TalkViewModel() }
}