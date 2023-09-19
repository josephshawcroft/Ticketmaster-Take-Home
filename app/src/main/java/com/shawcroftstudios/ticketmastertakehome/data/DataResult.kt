package com.shawcroftstudios.ticketmastertakehome.data

sealed class DataResult<out T> {
    object Loading : DataResult<Nothing>()
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error(val exception: Throwable) : DataResult<Nothing>()
}
