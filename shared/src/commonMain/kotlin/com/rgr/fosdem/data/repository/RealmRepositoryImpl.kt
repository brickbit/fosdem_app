package com.rgr.fosdem.data.repository

import com.rgr.fosdem.data.model.dao.BuildingDao
import com.rgr.fosdem.data.model.dao.EventDao
import com.rgr.fosdem.data.model.dao.RoomDao
import com.rgr.fosdem.data.model.dao.SpeakerDao
import com.rgr.fosdem.data.model.dao.StandDao
import com.rgr.fosdem.data.model.dao.StandFeaturesDao
import com.rgr.fosdem.data.model.dao.TalkDao
import com.rgr.fosdem.data.model.dao.TrackDao
import com.rgr.fosdem.data.model.dao.toBo
import com.rgr.fosdem.data.model.dao.toDao
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.repository.RealmRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class RealmRepositoryImpl: RealmRepository {

    val realm: Realm by lazy {
        val configuration = RealmConfiguration.create(
            schema = setOf(
                TrackDao::class,
                StandDao::class,
                StandFeaturesDao::class,
                EventDao::class,
                SpeakerDao::class,
                TalkDao::class,
                RoomDao::class,
                BuildingDao::class
            )

        )
        Realm.open(configuration)
    }

    override suspend fun getSchedule(): List<TrackBo> {
        return realm.query<TrackDao>().find().map {
            it.toBo()
        }

    }

    override suspend fun saveSchedule(schedules: List<TrackBo>){
        realm.writeBlocking {
            deleteAll()
        }
        schedules.map {
            realm.writeBlocking {
                copyToRealm(it.toDao())
            }
        }
    }
}