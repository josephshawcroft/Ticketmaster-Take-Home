package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import kotlinx.coroutines.flow.Flow
interface LocalEventListRepository {
    fun fetchEventsForCity(city: String): Flow<DataLoadingResult<List<Event>>>
    suspend fun insertEvents(events: List<Event>)
}