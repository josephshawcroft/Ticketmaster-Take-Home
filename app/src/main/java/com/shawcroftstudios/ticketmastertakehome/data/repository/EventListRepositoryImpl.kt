package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.network.ApiClient
import javax.inject.Inject

class EventListRepositoryImpl @Inject constructor(private val apiClient: ApiClient) :
    EventListRepository {
    override suspend fun fetchEventsForCity(city: String): Result<Any> {
        val response = apiClient.fetchEventsByCity(city)
        return if (response.isSuccessful) {
            Result.success(response.body() ?: "")
        } else Result.failure(Exception()) // todo complete properly
    }
}