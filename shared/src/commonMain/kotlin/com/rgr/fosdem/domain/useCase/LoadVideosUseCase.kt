package com.rgr.fosdem.domain.useCase

import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.bo.VideoBo
import com.rgr.fosdem.domain.model.xml.ScheduleDtoXml
import com.rgr.fosdem.domain.repository.DatabaseRepository
import com.rgr.fosdem.domain.repository.NetworkRepository
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML

class LoadVideosUseCase(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
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
        if(!getVideosFromDB()) {
            if (getSchedulesFromDB()) {
                return Result.success(Unit)
            } else {
                val stringSchedules = getSchedulesFromNetwork()
                stringSchedules.getOrNull()?.let {
                    val parsedData = parseXml(it)
                    parsedData.getOrNull()?.let { xmlData ->
                        val videos = getVideos(xmlData)
                        databaseRepository.saveVideos(videos)
                        return Result.success(Unit)
                    } ?: return Result.failure(ErrorType.ParseXmlError)
                } ?: return Result.failure(stringSchedules.exceptionOrNull()!!)
            }
        } else {
            return Result.success(Unit)
        }
    }

    //INPUT: nothing
    //OUTPUT: true if videos are saved in db false otherwise
    //PRE: nothing
    private suspend fun getVideosFromDB(): Boolean {
        databaseRepository.getVideos().getOrNull()?.let {
            return it.isNotEmpty()
        } ?: return false
    }

    //INPUT: nothing
    //OUTPUT: true if schedules are saved in db false otherwise
    //PRE: nothing
    private suspend fun getSchedulesFromDB(): Boolean {
        databaseRepository.getSchedule().getOrNull()?.let {
            return it.isNotEmpty()
        } ?: return false
    }

    //INPUT: nothing
    //OUTPUT: String with data if schedules are obtained from network
    //THROWS: network error if data has not been obtained, empty schedule if data obtained is empty
    //PRE: nothing
    private suspend fun getSchedulesFromNetwork(): Result<String> {
        val data = networkRepository.loadScheduleData()
        data.getOrNull()?.let { schedules ->
            if(schedules.isNotEmpty()) {
                return Result.success(schedules)
            }
            return Result.failure(ErrorType.EmptyScheduleListError)
        } ?: return Result.failure(ErrorType.UnknownNetworkError)
    }

    //INPUT: xml data in String format, not empty
    //OUTPUT: Object with XML data
    //THROWS: parser error if it is not capable to parse input string
    //PRE: data has been obtained in String format
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

    //INPUT: xml data in Object format
    //OUTPUT: List of videos and it is not empty
    //PRE: data has been obtained in XML object
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