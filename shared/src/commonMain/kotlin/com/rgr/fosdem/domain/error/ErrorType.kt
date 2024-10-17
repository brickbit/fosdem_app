package com.rgr.fosdem.domain.error

sealed class ErrorType: Throwable() {
    data class HttpError(val code: Int): ErrorType()
    data class UnknownError(val error: String): ErrorType()
    data object ParseError: ErrorType()
    data object ParseXmlError: ErrorType()
    data object ParseHtmlError: ErrorType()
    data object TimeOutError: ErrorType()
    data object UnknownHostException: ErrorType()
    data object UnknownNetworkError: ErrorType()
    data object EmptyScheduleListError: ErrorType()
    data object EmptyVideoListError: ErrorType()
    data object EmptyStandListError: ErrorType()
    data object EmptySpeakerListError: ErrorType()
    data object NoHoursFoundError: ErrorType()
    data object NoDaysFoundError: ErrorType()
    data object NoTracksFoundError: ErrorType()
    data object NoRoomsFoundError: ErrorType()
}

fun ErrorType.getString(): String {
    return when(this){
        is ErrorType.HttpError -> "Error ${this.code}"
        ErrorType.ParseError -> "Error parseando los datos"
        ErrorType.ParseXmlError -> "Error parseando los datos"
        ErrorType.ParseHtmlError -> "Error parseando los datos"
        ErrorType.TimeOutError -> "Time out"
        is ErrorType.UnknownError -> "Error desconocido"
        ErrorType.UnknownHostException -> "Host desconocido"
        ErrorType.UnknownNetworkError -> "Error de red desconocido"
        ErrorType.EmptyScheduleListError -> "No se encontraron eventos"
        ErrorType.EmptyVideoListError -> "No se encontraron videos"
        ErrorType.NoDaysFoundError -> "No se encontraron días"
        ErrorType.NoHoursFoundError -> "No se encontraron horas"
        ErrorType.NoRoomsFoundError -> "No se encontraron salas"
        ErrorType.NoTracksFoundError -> "No se encontraron tracks"
        ErrorType.EmptyStandListError -> "No se encontraron stands"
        ErrorType.EmptySpeakerListError -> "No se encontraron ponentes"
    }
}
