package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.domain.model.Event

interface EventListRepository {
    suspend fun fetchEventsForCity(city: String) : Result<List<Event>>
}