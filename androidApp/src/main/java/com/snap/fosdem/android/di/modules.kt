package com.snap.fosdem.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.snap.fosdem.android.provider.ActivityProvider
import com.snap.fosdem.android.provider.LanguageProviderImpl
import com.snap.fosdem.app.viewModel.LanguageViewModel
import com.snap.fosdem.app.viewModel.MainActivityViewModel
import com.snap.fosdem.app.viewModel.MainViewModel
import com.snap.fosdem.app.viewModel.OnBoardingViewModel
import com.snap.fosdem.app.viewModel.PreferencesViewModel
import com.snap.fosdem.app.viewModel.ScheduleViewModel
import com.snap.fosdem.app.viewModel.SettingsViewModel
import com.snap.fosdem.app.viewModel.SplashViewModel
import com.snap.fosdem.app.viewModel.TalkViewModel
import com.snap.fosdem.data.local.SETTINGS_PREFERENCES
import com.snap.fosdem.data.local.dataStorePreferences
import com.snap.fosdem.domain.provider.LanguageProvider
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
    factory<LanguageProvider> { LanguageProviderImpl(context) }
    single { ActivityProvider() }
}

val viewModelModules = module {
    viewModel { MainActivityViewModel() }
    viewModel { SplashViewModel(get(), get(), get(), get()) }
    viewModel { OnBoardingViewModel(get()) }
    viewModel { PreferencesViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { TalkViewModel(get(), get(), get()) }
    viewModel { LanguageViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }

}