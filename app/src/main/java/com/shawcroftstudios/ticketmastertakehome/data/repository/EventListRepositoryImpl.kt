package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.data.network.ApiClient
import javax.inject.Inject

class EventListRepositoryImpl @Inject constructor(private val apiClient: ApiClient) :
    EventListRepository {
    override suspend fun fetchEventsForCity(city: String): Result<List<Event>> {
        val response = apiClient.fetchEventsByCity(city)
        return if (response.isSuccessful) {
            // TODO add mapping here
            Result.success(emptyList())
        } else Result.failure(Exception()) // todo complete properly
    }
}