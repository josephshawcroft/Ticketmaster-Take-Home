package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventListRepository {
    fun fetchEventsForCity(city: String): Flow<DataLoadingResult<List<Event>>>
}