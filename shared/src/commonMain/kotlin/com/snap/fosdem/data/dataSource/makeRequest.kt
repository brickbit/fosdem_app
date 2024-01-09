package com.snap.fosdem.data.dataSource

import com.snap.fosdem.domain.error.ErrorType
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

suspend fun makeRequest(
    apiRequest: suspend () -> HttpResponse,
): Result<HttpResponse> {
    return try {
        val response = apiRequest()
        if(response.status.isSuccess()) {
            Result.success(response)
        } else {
            Result.failure(ErrorType.HttpError(response.status.value))
        }
    } catch (e: SocketTimeoutException) {
        Result.failure(ErrorType.TimeOutError)
    } catch (e: ErrorType.UnknownHostException) {
        Result.failure(ErrorType.UnknownHostException)
    } catch (e: Exception) {
        Result.failure(ErrorType.UnknownNetworkError)
    }
}

fun <T,V>transform(
    data: T,
    mapper: (T) -> V?
): Result<V> {
    return try {
        val result = mapper(data)
        if(result != null){
            Result.success(result)
        } else {
            Result.failure(ErrorType.ParseError)
        }
    } catch (e: Exception) {
        Result.failure(ErrorType.ParseError)
    }
}
