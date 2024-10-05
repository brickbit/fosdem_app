package com.rgr.fosdem.domain.useCase

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.rgr.fosdem.domain.repository.InMemoryRepository
import com.rgr.fosdem.domain.repository.NetworkRepository

class LoadStandsUseCase(
    private val networkRepository: NetworkRepository,
    private val inMemoryRepository: InMemoryRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        val html = networkRepository.loadStandsData()
        html.getOrNull()?.let {
            val document: Document = Ksoup.parse(html = it)
            val standElements = document.select(
                "#main .container-fluid .row-fluid"
            )
            val title = standElements.select("h3").map { it.text() }
            val image = standElements.select("img").map {  it.attr("src") }

            val features: MutableList<List<StandFeaturesBo>> = mutableListOf()
            features.add(getStandsFeature(1)+getStandsFeature(2)+getStandsFeature(3))
            features.add(getStandsFeature(5))
            features.add(getStandsFeature(7))
            features.add(getStandsFeature(9))

            val stands = title.mapIndexed { index, item ->
                StandBo(title = title[index], image = "https://fosdem.org"+image[index], features = features[index].toList())
            }
            inMemoryRepository.saveStandList(stands)
            return Result.success(Unit)
        }
        return Result.failure(Error())
    }

    private suspend fun getStandsFeature(index: Int): List<StandFeaturesBo> {
        val html = networkRepository.loadStandsData()
        html.getOrNull()?.let {
            val document: Document = Ksoup.parse(html = it)
            val features = mutableListOf<StandFeaturesBo>()
            val standElements = document.select(
                "#main .container-fluid .row-fluid"
            )

            val subtitles = standElements[index].select("h4").text()

            standElements[index].select("tr").select("td").mapIndexed { index, item ->
                if (item.select("h5").text().isNotEmpty()) {
                    features.add(
                        StandFeaturesBo(
                            type = item.select("h5").text(),
                            subtitle = subtitles,
                            companies = mutableListOf()
                        )
                    )
                } else {
                    features[features.size - 1].companies.add(item.select("td").text())
                }
            }
            features.map { feature -> feature.companies.removeAll { it.all { char -> char.isDigit() } } }
            return features.toList()
        }
        return emptyList()
    }
}

data class StandBo(
    val title: String,
    val image: String,
    val features: List<StandFeaturesBo>
)

data class StandFeaturesBo(
    val subtitle: String,
    val type: String,
    val companies: MutableList<String>
)