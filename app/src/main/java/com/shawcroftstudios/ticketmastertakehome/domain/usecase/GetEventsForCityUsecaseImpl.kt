package com.shawcroftstudios.ticketmastertakehome.domain.usecase

import com.shawcroftstudios.ticketmastertakehome.data.DataResult
import com.shawcroftstudios.ticketmastertakehome.data.DataSource
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
    override fun execute(cityName: String): Flow<DataResult<List<Event>>> {

        val localData: Flow<DataResult<List<Event>>> =
            localRepository.fetchEventsForCity(cityName)
        val remoteData: Flow<DataResult<List<Event>>> =
            remoteRepository.fetchEventsForCity(cityName)

        var hasUpdatedLocalDb = false

        return combine(localData, remoteData) { localResult, remoteResult ->
            when {
                remoteResult is DataResult.Success && remoteResult.data.isNotEmpty() -> {
                    if (!hasUpdatedLocalDb) {
                        localRepository.insertEvents(remoteResult.data)
                        hasUpdatedLocalDb = true
                    }
                    remoteResult
                }

                remoteResult is DataResult.Success -> DataResult.Error(
                    NoAvailableEventsException() // ie remote data is empty
                )

                remoteResult is DataResult.Loading && localResult is DataResult.Error -> DataResult.Loading()
                remoteResult is DataResult.Loading && localResult is DataResult.Success -> DataResult.Loading(
                    localResult.data,
                    DataSource.Local
                )

                remoteResult is DataResult.Error && localResult is DataResult.Success -> DataResult.Success(
                    localResult.data,
                    DataSource.LocalFallback
                )

                remoteResult is DataResult.Error && localResult is DataResult.Error -> DataResult.Error(
                    NoAvailableEventsException()
                )

                else -> localResult
            }
        }.flowOn(dispatcherProvider.io)
    }
}