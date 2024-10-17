package com.snap.fosdem.app.di

import android.content.Context
import com.rgr.fosdem.data.dataSource.db.getDatabaseBuilder
import org.koin.dsl.module

fun androidCommonModule(context: Context) = module {
    single { getDatabaseBuilder(context) }
}

