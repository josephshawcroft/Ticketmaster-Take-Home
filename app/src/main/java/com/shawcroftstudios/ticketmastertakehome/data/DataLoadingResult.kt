package com.shawcroftstudios.ticketmastertakehome.data

sealed class DataLoadingResult<out T> {
    data class Loading(val loadingMessage: String? = "Loading...") : DataLoadingResult<Nothing>()
    data class Success<T>(val data: T) : DataLoadingResult<T>()
    data class Error(val errorMessage: String) : DataLoadingResult<Nothing>()
}
