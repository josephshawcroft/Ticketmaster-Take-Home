package com.shawcroftstudios.ticketmastertakehome.data

sealed class DataResult<out T> {
    data class Loading<T>(val data: T? = null, val dataSource: DataSource? = null) : DataResult<T>() {
        init {
            require((data == null && dataSource == null) || (data != null && dataSource != null)) {
                "Loading state has to have both data with data source, or neither present"
            }
        }
    }

    data class Success<T>(val data: T, val dataSource: DataSource) : DataResult<T>()
    data class Error(val exception: Throwable) : DataResult<Nothing>()
}

sealed class DataSource {
    object Local : DataSource()
    object Remote : DataSource()
    object LocalFallback :
        DataSource() // Used to denote local data when remote data load has failed
}