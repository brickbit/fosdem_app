package com.snap.fosdem.data.repository

import com.snap.fosdem.data.Constant
import com.snap.fosdem.data.dataSource.makeRequest
import com.snap.fosdem.data.dataSource.transform
import com.snap.fosdem.data.model.TrackDto
import com.snap.fosdem.data.model.toBo
import com.snap.fosdem.domain.model.TrackBo
import com.snap.fosdem.domain.repository.ScheduleRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ScheduleRepositoryImpl: ScheduleRepository {

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }
    override suspend fun getSchedule(): Result<List<TrackBo>> {
        val apiResponse = makeRequest { client.get(Constant.BASE_URL+Constant.GET_TRACKS) }
        apiResponse.onFailure {
            return Result.failure(it)
        }
        return transform(apiResponse.getOrNull()?.body<List<TrackDto>>()) { response ->
            response?.map { it.toBo() }
        }
    }
}