package com.snap.fosdem.app.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.app.viewModel.OnBoardingViewModel
import com.snap.fosdem.app.viewModel.PreferencesViewModel
import com.snap.fosdem.app.viewModel.SettingsViewModel
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.viewModel.TalkViewModel
import com.snap.fosdem.data.local.dataStorePreferences
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
val viewModelModules = module {
    single { SplashViewModel(get(), get(), get()) }
    single { OnBoardingViewModel(get()) }
    single { PreferencesViewModel(get(), get()) }
    single { MainViewModel(get(),get(), get(), get()) }
    single { SettingsViewModel() }
    single { TalkViewModel(get()) }
}

object GetViewModels: KoinComponent {
    fun getSplashViewModel() = get<SplashViewModel>()
    fun getOnBoardingViewModel() = get<OnBoardingViewModel>()
    fun getPreferencesViewModel() = get<PreferencesViewModel>()
    fun getMainViewModel() = get<MainViewModel>()
    fun getSpeakerViewModel() = get<SettingsViewModel>()
    fun getTalkViewModel() = get<TalkViewModel>()
}