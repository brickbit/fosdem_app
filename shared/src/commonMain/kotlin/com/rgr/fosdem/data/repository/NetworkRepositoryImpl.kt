package com.rgr.fosdem.data.repository

import com.rgr.fosdem.data.Constant
import com.rgr.fosdem.data.dataSource.makeRequest
import com.rgr.fosdem.data.dataSource.transform
import com.rgr.fosdem.data.model.dto.TrackDto
import com.rgr.fosdem.data.model.dto.VersionDto
import com.rgr.fosdem.data.model.dto.toBo
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.VersionBo
import com.rgr.fosdem.domain.repository.NetworkRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NetworkRepositoryImpl: NetworkRepository {

    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 0 // 0 to disable, or a millisecond value to fit your needs
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    print("KTOR: $message")
                }
            }
            level = LogLevel.ALL
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

    override suspend fun getVersion(): Result<VersionBo> {
        val apiResponse = makeRequest { client.get(Constant.BASE_URL+Constant.GET_VERSION) }
        apiResponse.onFailure {
            return Result.failure(it)
        }
        return transform(apiResponse.getOrNull()?.body<VersionDto>()) { response ->
            response?.toBo()
        }
    }

    override suspend fun loadData(): Result<String> {
        val apiResponse = makeRequest { client.get(Constant.PRETAL_SCHEDULE_URL) }
        apiResponse.onFailure {
            return Result.failure(it)
        }
        return apiResponse.getOrNull()?.let {
            Result.success(it.body())
        } ?: Result.failure(Error())
    }
}