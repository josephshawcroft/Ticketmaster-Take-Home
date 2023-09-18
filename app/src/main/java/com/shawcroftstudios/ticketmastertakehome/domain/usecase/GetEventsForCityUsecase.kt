package com.shawcroftstudios.ticketmastertakehome.domain.usecase

import com.shawcroftstudios.ticketmastertakehome.domain.model.Event

interface GetEventsForCityUsecase {

    suspend fun execute(cityName: String): Result<List<Event>>
}