package com.rgr.fosdem.android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleAndroidDao {
    @Query("SELECT * FROM schedule")
    fun fetchSchedules(): Flow<List<ScheduleAndroidEntity>>

    @Query("SELECT * FROM schedule WHERE id = :id")
    fun findScheduleId(id: Int): Flow<ScheduleAndroidEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(schedules: List<ScheduleAndroidEntity>)
}