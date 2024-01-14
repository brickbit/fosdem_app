package com.snap.fosdem.android.app

import android.app.Application
import com.snap.fosdem.android.di.providerModule
import com.snap.fosdem.android.di.viewModelModules
import com.snap.fosdem.app.di.repositoryModule
import com.snap.fosdem.app.di.useCaseModule
import org.koin.core.context.startKoin

class FosdemApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                providerModule(this@FosdemApp),
                repositoryModule,
                useCaseModule,
                viewModelModules
            )
        }
    }
}