package com.shawcroftstudios.ticketmastertakehome.domain.usecase

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.repository.LocalEventListRepository
import com.shawcroftstudios.ticketmastertakehome.data.repository.RemoteEventListRepository
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetEventsForCityUsecaseImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val localRepository: LocalEventListRepository,
    private val remoteRepository: RemoteEventListRepository,
) : GetEventsForCityUsecase {
    override fun execute(cityName: String): Flow<DataLoadingResult<List<Event>>> {
        val localData: Flow<DataLoadingResult<List<Event>>> = localRepository.fetchEventsForCity(cityName)
        val remoteData: Flow<DataLoadingResult<List<Event>>> = remoteRepository.fetchEventsForCity(cityName)

        return combine(localData, remoteData) { localResult, remoteResult ->
            if (remoteResult is DataLoadingResult.Success && remoteResult.data.isNotEmpty()) {
                localRepository.insertEvents(remoteResult.data)
                remoteResult
            } else if (localResult is DataLoadingResult.Error && remoteResult is DataLoadingResult.Loading) {
                DataLoadingResult.Loading // if local DB has errored but remote is still loading, still treat as loading
            } else {
                localResult // otherwise fallback to local DB
            }
        }.flowOn(dispatcherProvider.io)
    }
}