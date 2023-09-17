package com.shawcroftstudios.ticketmastertakehome.domain.usecase

interface GetEventsForCityUsecase {

    suspend fun execute(cityName: String): Result<Any>
}