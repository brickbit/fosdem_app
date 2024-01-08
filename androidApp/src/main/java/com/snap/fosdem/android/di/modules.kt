package com.snap.fosdem.android.di

import com.snap.fosdem.app.viewModel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { SplashViewModel() }
}