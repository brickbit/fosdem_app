package com.rgr.fosdem.android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoAndroidDao {
    @Query("SELECT * FROM videos")
    fun fetchVideos(): Flow<List<VideoAndroidEntity>>

    @Query("SELECT * FROM videos WHERE idTalk = :id")
    fun findVideoId(id: Int): Flow<VideoAndroidEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(videos: List<VideoAndroidEntity>)
}