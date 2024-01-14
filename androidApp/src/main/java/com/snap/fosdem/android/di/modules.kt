package com.snap.fosdem.android.di

import android.content.Context
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
import com.snap.fosdem.data.local.SETTINGS_PREFERENCES
import com.snap.fosdem.data.local.dataStorePreferences
import com.snap.fosdem.data.repository.ScheduleRepositoryImpl
import com.snap.fosdem.domain.repository.LocalRepository
import com.snap.fosdem.domain.repository.ScheduleRepository
import com.snap.fosdem.domain.useCase.GetOnBoardingStatusUseCase
import com.snap.fosdem.domain.useCase.GetPreferredTracksUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByBuildingUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByHourUseCase
import com.snap.fosdem.domain.useCase.GetScheduleByTrackUseCase
import com.snap.fosdem.domain.useCase.GetScheduleDataUseCase
import com.snap.fosdem.domain.useCase.GetTracksUseCase
import com.snap.fosdem.domain.useCase.SaveOnBoardingUseCase
import com.snap.fosdem.domain.useCase.SavePreferredTracksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File

fun providerModule(context: Context) = module {
    single<DataStore<Preferences>> {
        dataStorePreferences(
            corruptionHandler = null,
            coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            migrations = emptyList(),
            calculatedPath = File(context.filesDir, "datastore/$SETTINGS_PREFERENCES").path
        )
    }
}

val viewModelModules = module {
    viewModel { MainActivityViewModel() }
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { OnBoardingViewModel(get()) }
    viewModel { PreferencesViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { SpeakerViewModel() }
    viewModel { TalkViewModel() }
}