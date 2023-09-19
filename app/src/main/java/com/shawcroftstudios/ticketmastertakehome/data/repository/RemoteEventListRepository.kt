package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.data.DataResult
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import kotlinx.coroutines.flow.Flow
interface RemoteEventListRepository {
    fun fetchEventsForCity(city: String): Flow<DataResult<List<Event>>>
}