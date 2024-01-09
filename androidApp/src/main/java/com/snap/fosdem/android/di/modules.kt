package com.snap.fosdem.android.di

import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.app.viewModel.OnBoardingViewModel
import com.snap.fosdem.app.viewModel.PreferencesViewModel
import com.snap.fosdem.app.viewModel.SpeakerViewModel
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.viewModel.TalkViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    factory<ScheduleRepository> { ScheduleRepositoryImpl() }
}
val useCaseModule = module {
    single { GetScheduleDataUseCase(get()) }
}
val viewModelModules = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { OnBoardingViewModel() }
    viewModel { PreferencesViewModel() }
    viewModel { MainViewModel() }
    viewModel { SpeakerViewModel() }
    viewModel { TalkViewModel() }
}