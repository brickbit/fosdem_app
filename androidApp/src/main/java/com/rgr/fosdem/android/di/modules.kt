package com.rgr.fosdem.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.rgr.fosdem.android.provider.ActivityProvider
import com.rgr.fosdem.android.provider.JsonProviderImpl
import com.rgr.fosdem.app.viewModel.LanguageViewModel
import com.rgr.fosdem.app.viewModel.MainActivityViewModel
import com.rgr.fosdem.app.viewModel.MainViewModel
import com.rgr.fosdem.app.viewModel.OnBoardingViewModel
import com.rgr.fosdem.app.viewModel.PreferencesViewModel
import com.rgr.fosdem.app.viewModel.ScheduleViewModel
import com.rgr.fosdem.app.viewModel.SettingsViewModel
import com.rgr.fosdem.app.viewModel.SplashViewModel
import com.rgr.fosdem.app.viewModel.TalkViewModel
import com.rgr.fosdem.data.local.SETTINGS_PREFERENCES
import com.rgr.fosdem.data.local.dataStorePreferences
import com.rgr.fosdem.domain.provider.ConnectivityProvider
import com.rgr.fosdem.domain.provider.LanguageProvider
import com.rgr.fosdem.android.provider.LanguageProviderImpl
import com.rgr.fosdem.android.provider.NetworkConnectivityProvider
import com.rgr.fosdem.app.viewModel.ListEventsViewModel
import com.rgr.fosdem.domain.repository.JsonProvider
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
    factory<JsonProvider> { JsonProviderImpl(context) }
    factory<LanguageProvider> { LanguageProviderImpl(get()) }
    factory<ConnectivityProvider> { NetworkConnectivityProvider(context) }
    single { ActivityProvider() }
}

val viewModelModules = module {
    viewModel { MainActivityViewModel(get()) }
    viewModel { SplashViewModel(get(), get(), get(), get(), get()) }
    viewModel { OnBoardingViewModel(get()) }
    viewModel { PreferencesViewModel(get(), get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { TalkViewModel(get(), get(), get()) }
    viewModel { LanguageViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get()) }
    viewModel { ListEventsViewModel(get(), get(), get(), get()) }
}