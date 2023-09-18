package com.shawcroftstudios.ticketmastertakehome.data.repository

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.database.EventDao
import com.shawcroftstudios.ticketmastertakehome.data.mapper.EventMapper
import com.shawcroftstudios.ticketmastertakehome.data.network.EventApi
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EventListRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val eventDao: EventDao,
    private val eventMapper: EventMapper,
    private val dispatcherProvider: DispatcherProvider,
) : EventListRepository {
    override fun fetchEventsForCity(city: String): Flow<DataLoadingResult<List<Event>>> {

        val localData: Flow<DataLoadingResult<List<Event>>> = fetchLocalEventsForCity(city)
        val remoteData: Flow<DataLoadingResult<List<Event>>> = fetchRemoteEventsForCity(city)

        return combine(localData, remoteData) { localResult, remoteResult ->
            if (remoteResult is DataLoadingResult.Success && remoteResult.data.isNotEmpty()) {
                remoteResult
            } else {
                localResult
            }
        }.flowOn(dispatcherProvider.io)
    }

    private fun fetchLocalEventsForCity(city: String) = flow {
        val localEvents = eventDao.getEventsForCity(city)
        if (localEvents.isNotEmpty()) {
            emit(DataLoadingResult.Success(localEvents))
        } else emit(DataLoadingResult.Loading())
    }

    private fun fetchRemoteEventsForCity(city: String) = flow {
        val response = eventApi.fetchEventsForCity(city)
        val data = response.getOrNull()
        if (response.isSuccess && data != null) {
            val events = eventMapper.mapToDomain(data)
            eventDao.insertAll(events)
            emit(DataLoadingResult.Success(events))
        } else {
            emit(DataLoadingResult.Error(response.exceptionOrNull()?.message ?: UNKNOWN_ERROR))
        }
    }

    private companion object {
        private const val UNKNOWN_ERROR = "An unknown error has occurred"
    }
}