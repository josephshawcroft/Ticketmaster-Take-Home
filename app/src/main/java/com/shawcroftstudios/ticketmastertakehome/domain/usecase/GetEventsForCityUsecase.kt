package com.shawcroftstudios.ticketmastertakehome.domain.usecase

import com.shawcroftstudios.ticketmastertakehome.data.DataLoadingResult
import com.shawcroftstudios.ticketmastertakehome.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface GetEventsForCityUsecase {

    fun execute(cityName: String): Flow<DataLoadingResult<List<Event>>>
}