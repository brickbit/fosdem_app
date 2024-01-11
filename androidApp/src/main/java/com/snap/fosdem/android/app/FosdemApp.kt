package com.snap.fosdem.android.app

import android.app.Application
import com.snap.fosdem.android.di.providerModule
import com.snap.fosdem.android.di.repositoryModule
import com.snap.fosdem.android.di.useCaseModule
import com.snap.fosdem.android.di.viewModelModules
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