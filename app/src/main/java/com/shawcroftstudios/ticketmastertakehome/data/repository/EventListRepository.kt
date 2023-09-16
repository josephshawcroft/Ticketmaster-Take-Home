package com.shawcroftstudios.ticketmastertakehome.data.repository

interface EventListRepository {
    suspend fun fetchEventsForCity(city: String) : Result<Any>
}