package com.rgr.fosdem.android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StandAndroidDao {
    @Query("SELECT * FROM stands")
    fun fetchStands(): Flow<List<StandAndroidEntity>>

    @Query("SELECT * FROM stands WHERE id = :id")
    fun findStandId(id: Int): Flow<StandAndroidEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(stands: List<StandAndroidEntity>)
}