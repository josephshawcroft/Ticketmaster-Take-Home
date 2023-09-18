package com.shawcroftstudios.ticketmastertakehome.data.network

import com.shawcroftstudios.ticketmastertakehome.data.response.EventsResponse
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class EventApi @Inject constructor(retrofit: Retrofit) {

    private val apiService: ApiService = retrofit.create(ApiService::class.java)
    suspend fun fetchEventsForCity(city: String): Response<EventsResponse> =
        apiService.fetchEventsForCity(name = city)
}