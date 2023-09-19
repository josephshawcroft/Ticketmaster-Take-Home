package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.database.EventDao
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalEventListRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
) : LocalEventListRepository {
    override fun fetchEventsForCity(city: String): Flow<DataLoadingResult<List<Event>>> = flow {
        emit(DataLoadingResult.Loading)
        val localEvents = eventDao.getEventsForCity(city)
        if (localEvents.isNotEmpty()) {
            emit(DataLoadingResult.Success(localEvents))
        } else emit(DataLoadingResult.Error(NoAvailableEventsException()))
    }

    override suspend fun insertEvents(events: List<Event>) {
        eventDao.insertAll(events)
    }
}