package com.snap.fosdem.domain.error

sealed class ErrorType: Throwable() {
    data class HttpError(val code: Int): ErrorType()
    data class UnknownError(val error: String): ErrorType()
    data object ParseError: ErrorType()
    data object TimeOutError: ErrorType()
    data object UnknownHostException: ErrorType()
    data object UnknownNetworkError: ErrorType()
}

fun ErrorType.getString(): String {
    return when(this){
        is ErrorType.HttpError -> "Error ${this.code}"
        ErrorType.ParseError -> "Error parseando los datos"
        ErrorType.TimeOutError -> "Time out"
        is ErrorType.UnknownError -> "Error desconocido"
        ErrorType.UnknownHostException -> "Host desconocido"
        ErrorType.UnknownNetworkError -> "Error de red desconocido"
    }
}
