package com.rgr.fosdem.data.dataSource.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
  val appContext = ctx.applicationContext
  val dbFile = appContext.getDatabasePath("fosdem.db")
  return Room.databaseBuilder<AppDatabase>(
    context = appContext,
    name = dbFile.absolutePath
  )
}