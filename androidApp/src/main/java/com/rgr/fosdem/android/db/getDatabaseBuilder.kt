package com.rgr.fosdem.android.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.rgr.fosdem.data.dataSource.db.DATABASE_NAME
import com.rgr.fosdem.data.dataSource.db.ScheduleDatabase
import com.rgr.fosdem.data.dataSource.db.dao.SchedulesDao

fun getScheduleDao(ctx: Context): SchedulesDao{
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<ScheduleDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver()).build().scheduleDao()
}