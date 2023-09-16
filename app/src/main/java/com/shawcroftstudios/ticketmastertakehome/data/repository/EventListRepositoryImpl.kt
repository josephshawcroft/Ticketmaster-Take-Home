package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.network.ApiClient
import javax.inject.Inject

class EventListRepositoryImpl @Inject constructor(private val apiClient: ApiClient) :
    EventListRepository {
    override suspend fun fetchEventsForCity(city: String): Result<Any> {
        return Result.success(true)
    }
}