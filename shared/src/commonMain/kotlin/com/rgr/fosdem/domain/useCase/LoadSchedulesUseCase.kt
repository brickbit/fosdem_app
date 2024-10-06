package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.data.dataSource.db.AppDatabase
import com.rgr.fosdem.data.model.entity.ScheduleEntity
import com.rgr.fosdem.data.model.entity.toEntity
import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.AttachmentBo
import com.rgr.fosdem.domain.model.bo.ScheduleBo
import com.rgr.fosdem.domain.model.bo.VideoBo
import com.rgr.fosdem.domain.model.xml.ScheduleDtoXml
import com.rgr.fosdem.domain.repository.InMemoryRepository
import com.rgr.fosdem.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.collectLatest
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML

class LoadSchedulesUseCase(
    private val networkRepository: NetworkRepository,
    private val inMemoryRepository: InMemoryRepository,
    private val databaseRepository: AppDatabase,
) {

    private val xml: XML by lazy {
        XML {
            xmlVersion = XmlVersion.XML10
            indentString = "  "
            repairNamespaces = true
            autoPolymorphic = true
        }
    }

    suspend operator fun invoke(): Result<Unit> {
        val schedulesDB = databaseRepository.getDao().fetchSchedules()
        var schedulesObtained = listOf<ScheduleEntity>()
        try {
            schedulesDB.collectLatest { item ->
                schedulesObtained = item
            }
        } catch (e: Exception) {
            print(e)
        }
        if(schedulesObtained.isEmpty()) {
            val data = networkRepository.loadScheduleData()
            data.getOrNull()?.let {
                val parsedData = parseXml(it)
                parsedData.getOrNull()?.let { xmlData ->
                    val videos = getVideos(xmlData)
                    val schedules = getSchedule(xmlData)
                    databaseRepository.getDao().save(schedules.map { it.toEntity() })
                    //val schedulesEntities = schedules.map { schedule -> schedule.toEntity() }
                    val schedulesSaved = inMemoryRepository.saveScheduleList(schedules)
                    schedulesSaved.getOrNull()?.let {
                        val videosSaved = inMemoryRepository.saveVideoList(videos)
                        videosSaved.getOrNull()?.let {
                            return Result.success(Unit)
                        } ?: return Result.failure(data.exceptionOrNull()!!)
                    } ?: return Result.failure(data.exceptionOrNull()!!)
                } ?: return Result.failure(data.exceptionOrNull()!!)
            } ?: return Result.failure(data.exceptionOrNull()!!)
        } else {
            return Result.success(Unit)
        }
    }

    private fun parseXml(apiData: String): Result<ScheduleDtoXml> {
        try {
            val schedule = xml.decodeFromString(
                deserializer = ScheduleDtoXml.serializer(),
                string = apiData
                    .replace("<br/>", "\n")
                    .replace("<abstract>","<abstract><![CDATA[")
                    .replace("</abstract>","]]></abstract>")
                    .replace("<title>","<title><![CDATA[")
                    .replace("</title>","]]></title>")
                    .replace("&", "&amp;")
            )
            return Result.success(schedule)
        } catch (e: Exception) {
            return Result.failure(ErrorType.ParseXmlError)
        }
    }

    private fun getSchedule(schedule: ScheduleDtoXml): List<ScheduleBo> {
        val scheduleList = mutableListOf<ScheduleBo>()
        schedule.day.map { day ->
            day.room.map { room ->
                room.event.map { event ->
                    scheduleList.add(
                        ScheduleBo(
                            id = event.id,
                            date = event.date,
                            start = event.start,
                            duration = event.duration,
                            title = event.title,
                            subtitle = event.subtitle ?: "",
                            track = event.track,
                            type = event.type,
                            language = event.language,
                            abstract = event.abstract,
                            description = event.description ?: "",
                            feedbackUrl = event.feedbackUrl,
                            attachment = event.attachments.attachment.map {
                                AttachmentBo(name = it.content, link = it.href)
                            },
                            speaker = event.persons.person.map { it.content },
                            room = room.name,
                            year = schedule.conference.start.split("-").getOrNull(0) ?: "",
                            favourite = false
                        )
                    )
                }
            }
        }
        return  scheduleList.toList()
    }

    private fun getVideos(schedule: ScheduleDtoXml): List<VideoBo> {
        val videos = mutableListOf<VideoBo>()
        schedule.day.map { day ->
            day.room.map { room ->
                room.event.map { event ->
                    event.links.link.map { link ->
                        if(link.content == "Video recording (mp4)") {
                            videos.add(
                                VideoBo(
                                    idTalk = event.id,
                                    link = link.href.replace("&amp;","&"),
                                    name = event.title,
                                    speakers = event.persons.person.map { it.content },
                                    type = event.type,
                                    year = schedule.conference.start.split("-").getOrNull(0) ?: ""
                                )
                            )
                        }
                    }
                }
            }
        }
        return videos.toList()
    }
}



























