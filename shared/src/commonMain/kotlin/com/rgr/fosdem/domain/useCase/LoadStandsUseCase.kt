package com.rgr.fosdem.domain.useCase

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.StandBo
import com.rgr.fosdem.domain.model.StandFeaturesBo
import com.rgr.fosdem.domain.repository.DatabaseRepository
import com.rgr.fosdem.domain.repository.NetworkRepository

class LoadStandsUseCase(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        if(!getStandsFromDB()) {
            val stands = getStandsFromNetwork()
            stands.getOrNull()?.let { stringStands ->
                val standsParsed = parseStands(stringStands)
                standsParsed.getOrNull()?.let {
                    databaseRepository.saveStands(it)
                    return Result.success(Unit)
                } ?: return Result.failure(standsParsed.exceptionOrNull()!!)
            } ?: return Result.failure(stands.exceptionOrNull()!!)
        } else {
            return Result.success(Unit)
        }
    }


    //INPUT: nothing
    //OUTPUT: true if stands are saved in db false otherwise
    //PRE: nothing
    private suspend fun getStandsFromDB(): Boolean {
        databaseRepository.getStands().getOrNull()?.let {
            return it.isNotEmpty()
        } ?: return false
    }

    //INPUT: nothing
    //OUTPUT: String with data if stands are obtained from network
    //THROWS: network error if data has not been obtained, empty stands if data obtained is empty
    //PRE: nothing
    private suspend fun getStandsFromNetwork(): Result<String> {
        val data = networkRepository.loadStandsData()
        data.getOrNull()?.let { stands ->
            if(stands.isNotEmpty()) {
                return Result.success(stands)
            }
            return Result.failure(ErrorType.EmptyStandListError)
        } ?: return Result.failure(ErrorType.UnknownNetworkError)
    }

    private suspend fun parseStands(standString: String): Result<List<StandBo>> {
        try {
            val document: Document = Ksoup.parse(html = standString)
            val standElements = document.select(
                "#main .container-fluid .row-fluid"
            )
            val title = standElements.select("h3").map { it.text() }
            val image = standElements.select("img").map { it.attr("src") }

            val features: MutableList<List<StandFeaturesBo>> = mutableListOf()
            features.add(getStandsFeature(1) + getStandsFeature(2) + getStandsFeature(3))
            features.add(getStandsFeature(5))
            features.add(getStandsFeature(7))
            features.add(getStandsFeature(9))

            val stands = title.mapIndexed { index, item ->
                StandBo(
                    title = title[index],
                    image = "https://fosdem.org" + image[index],
                    features = features[index].toList()
                )
            }
            return Result.success(stands)
        } catch (e: Exception) {
            return Result.failure(ErrorType.ParseHtmlError)
        }

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
                            companies = arrayListOf()
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
