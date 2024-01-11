package com.snap.fosdem.app.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(providerModule, repositoryModule, useCaseModule,viewModelModules)
    }
}