package com.rgr.fosdem.app.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.rgr.fosdem.data.dataSource.db.DATABASE_NAME
import com.rgr.fosdem.data.dataSource.db.ScheduleDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

fun getDatabaseBuilder(): RoomDatabase.Builder<ScheduleDatabase> {
    val dbFilePath = documentDirectory() + "/$DATABASE_NAME"
    return Room.databaseBuilder<ScheduleDatabase>(
        name = dbFilePath,  
    ).setDriver(BundledSQLiteDriver())
}