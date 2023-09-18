package com.shawcroftstudios.ticketmastertakehome.data.repository

import androidx.annotation.VisibleForTesting
import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.database.EventDao
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
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
            } else if (localResult is DataLoadingResult.Error && remoteResult is DataLoadingResult.Loading) {
                DataLoadingResult.Loading // if local DB has errored but remote is still loading, show spinner
            } else {
                localResult // otherwise fallback to local DB
            }
        }.flowOn(dispatcherProvider.io)
    }

    @VisibleForTesting
    fun fetchLocalEventsForCity(city: String) = flow {
        emit(DataLoadingResult.Loading)
        val localEvents = eventDao.getEventsForCity(city)
        if (localEvents.isNotEmpty()) {
            emit(DataLoadingResult.Success(localEvents))
        } else emit(DataLoadingResult.Error(NoAvailableEventsException()))
    }

    @VisibleForTesting
    fun fetchRemoteEventsForCity(city: String) = flow {
        emit(DataLoadingResult.Loading)
        val response = eventApi.fetchEventsForCity(city)
        val data = response.getOrNull()
        if (response.isSuccess && data != null) {
            val events = eventMapper.mapToDomain(data)
            emit(DataLoadingResult.Success(events))
            eventDao.insertAll(events)
        } else {
            emit(DataLoadingResult.Error(response.exceptionOrNull() ?: NoAvailableEventsException()))
        }
    }
}