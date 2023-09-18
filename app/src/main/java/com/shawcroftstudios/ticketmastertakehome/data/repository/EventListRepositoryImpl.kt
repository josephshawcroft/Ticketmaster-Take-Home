package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.data.mapper.EventMapper
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.data.network.ApiClient
import javax.inject.Inject

class EventListRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val eventMapper: EventMapper
) : EventListRepository {
    override suspend fun fetchEventsForCity(city: String): Result<List<Event>> {
        val response = apiClient.fetchEventsByCity(city)
        val body = response.body()

        return if (response.isSuccessful && body != null) {
            Result.success(eventMapper.mapToDomain(body))
        } else Result.failure(Exception())
    }
}