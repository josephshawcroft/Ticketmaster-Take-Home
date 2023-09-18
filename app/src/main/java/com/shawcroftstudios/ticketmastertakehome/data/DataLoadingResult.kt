package com.shawcroftstudios.ticketmastertakehome.data

sealed class DataLoadingResult<out T> {
    object Loading : DataLoadingResult<Nothing>()
    data class Success<T>(val data: T) : DataLoadingResult<T>()
    data class Error(val exception: Throwable) : DataLoadingResult<Nothing>()
}
