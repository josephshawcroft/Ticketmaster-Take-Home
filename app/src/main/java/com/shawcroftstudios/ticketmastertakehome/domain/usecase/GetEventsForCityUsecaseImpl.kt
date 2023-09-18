package com.shawcroftstudios.ticketmastertakehome.domain.usecase

import com.shawcroftstudios.ticketmastertakehome.data.repository.EventListRepository
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import com.shawcroftstudios.ticketmastertakehome.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEventsForCityUsecaseImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val repository: EventListRepository
): GetEventsForCityUsecase {
    override suspend fun execute(cityName: String): Result<List<Event>> =
        withContext(dispatcherProvider.io) {
            repository.fetchEventsForCity(cityName)
        }
}