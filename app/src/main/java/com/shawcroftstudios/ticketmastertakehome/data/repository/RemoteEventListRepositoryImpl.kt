package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
import com.shawcroftstudios.ticketmastertakehome.data.exception.UnknownException
import com.shawcroftstudios.ticketmastertakehome.data.mapper.EventMapper
import com.shawcroftstudios.ticketmastertakehome.data.network.EventApi
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteEventListRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val eventMapper: EventMapper,
) : RemoteEventListRepository {
    override fun fetchEventsForCity(city: String): Flow<DataLoadingResult<List<Event>>> = flow {
        emit(DataLoadingResult.Loading)
        val response = eventApi.fetchEventsForCity(city)
        val data = response.getOrNull()

        if (response.isSuccess && data != null) {
            val events = eventMapper.mapToDomain(data)

            if (events.isEmpty()) {
                emit(DataLoadingResult.Error(NoAvailableEventsException()))
            } else {
                emit(DataLoadingResult.Success(events))
            }
        } else {
            val exception = response.exceptionOrNull() ?: UnknownException()
            emit(DataLoadingResult.Error(exception))
        }
    }
}