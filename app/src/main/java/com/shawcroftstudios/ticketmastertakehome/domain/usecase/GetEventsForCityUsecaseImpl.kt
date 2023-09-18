package com.shawcroftstudios.ticketmastertakehome.domain.usecase

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.data.repository.EventListRepository
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetEventsForCityUsecaseImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val repository: EventListRepository
) : GetEventsForCityUsecase {
    override fun execute(cityName: String): Flow<DataLoadingResult<List<Event>>> =
        repository.fetchEventsForCity(cityName).flowOn(dispatcherProvider.io)
}