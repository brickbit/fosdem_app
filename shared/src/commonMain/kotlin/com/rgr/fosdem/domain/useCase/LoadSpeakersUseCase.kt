package com.rgr.fosdem.domain.useCase

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.SpeakerBo
import com.rgr.fosdem.domain.repository.InMemoryRepository
import com.rgr.fosdem.domain.repository.NetworkRepository

class LoadSpeakersUseCase(
    private val networkRepository: NetworkRepository,
    private val inMemoryRepository: InMemoryRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        getSpeakers()
        return Result.success(Unit)
    }

    private suspend fun getSpeakers(): Result<List<SpeakerBo>> {
        val speakerList: MutableList<SpeakerBo> = mutableListOf()
        val html = networkRepository.loadSpeakersData()
        html.getOrNull()?.let {
            val document: Document = Ksoup.parse(html = it)
            val listSpeakers = document.select(".name-index-items")
            listSpeakers.map {
                val speakersForTalk = it.select("a")
                speakersForTalk.mapIndexed { index,value ->
                    val speakerUrl = value.attr("href")
                    getSpeaker(speakerUrl, index).getOrNull()?.let { speaker ->
                        speakerList.add(speaker)
                    }
                }
            }
        }
        return Result.success(speakerList)
    }

    private suspend fun getSpeaker(url: String, index: Int):Result<SpeakerBo> {
        val html = networkRepository.loadSpeakerItemData(url)
        print(html)
        html.getOrNull()?.let {
            html.getOrNull()?.let {
                val document = Ksoup.parse(html = it)
                val name = document.select("#main")[0].select("#pagetitles h1")[0].text()
                val image = document.select("#main").getOrNull(0)?.select("img")?.getOrNull(0)
                    ?.attr("src")?.let { "https://fosdem.org$it" }
                val description = document.select("#main").getOrNull(0)?.select("p")
                    ?.joinToString("\n") {
                        it.text()
                    }
                return Result.success(SpeakerBo(
                    id = index.toString(),
                    name = name,
                    image = image,
                    description = description
                ))
            }
        }
        return Result.failure(ErrorType.ParseError)
    }
}