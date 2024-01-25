package com.rgr.fosdem.data.repository

import com.rgr.fosdem.data.Constant
import com.rgr.fosdem.data.dataSource.makeRequest
import com.rgr.fosdem.data.dataSource.transform
import com.rgr.fosdem.data.model.dto.TrackDto
import com.rgr.fosdem.data.model.dto.VersionDto
import com.rgr.fosdem.data.model.dto.toBo
import com.rgr.fosdem.domain.error.ErrorType
import com.rgr.fosdem.domain.model.TrackBo
import com.rgr.fosdem.domain.model.VersionBo
import com.rgr.fosdem.domain.repository.ScheduleRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
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
}