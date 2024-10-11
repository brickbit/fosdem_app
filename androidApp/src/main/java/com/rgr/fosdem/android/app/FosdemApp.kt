package com.rgr.fosdem.android.app

import android.app.Application
import com.rgr.fosdem.android.di.providerModule
import com.rgr.fosdem.android.di.viewModelModules
import com.rgr.fosdem.app.di.repositoryModule
import com.rgr.fosdem.app.di.useCaseModule
import com.snap.fosdem.app.di.androidCommonModule
import org.koin.core.context.startKoin

class FosdemApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                androidCommonModule(this@FosdemApp),
                providerModule(this@FosdemApp),
                repositoryModule,
                useCaseModule,
                viewModelModules
            )
        }
    }
}