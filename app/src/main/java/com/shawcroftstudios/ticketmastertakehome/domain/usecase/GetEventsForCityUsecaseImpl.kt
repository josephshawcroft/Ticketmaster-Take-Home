package com.shawcroftstudios.ticketmastertakehome.domain.usecase

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.exception.NoAvailableEventsException
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

        val localData: Flow<DataLoadingResult<List<Event>>> =
            localRepository.fetchEventsForCity(cityName)
        val remoteData: Flow<DataLoadingResult<List<Event>>> =
            remoteRepository.fetchEventsForCity(cityName)

        var hasUpdatedLocalDb = false

        return combine(localData, remoteData) { localResult, remoteResult ->
            when {
                remoteResult is DataLoadingResult.Success && remoteResult.data.isNotEmpty() -> {
                    if (!hasUpdatedLocalDb) {
                        localRepository.insertEvents(remoteResult.data)
                        hasUpdatedLocalDb = true
                    }
                    remoteResult
                }

                remoteResult is DataLoadingResult.Success -> DataLoadingResult.Error(
                    NoAvailableEventsException() // ie remote data is empty
                )
                remoteResult is DataLoadingResult.Loading && localResult is DataLoadingResult.Error -> remoteResult // Use local data if it's available
                else -> localResult
            }
        }.flowOn(dispatcherProvider.io)
    }
}